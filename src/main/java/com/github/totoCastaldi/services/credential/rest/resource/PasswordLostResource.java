package com.github.totoCastaldi.services.credential.rest.resource;

import com.github.totoCastaldi.restServer.TimeProvider;
import com.github.totoCastaldi.restServer.response.ApiResponse;
import com.github.totoCastaldi.services.credential.rest.model.UserDao;
import com.github.totoCastaldi.services.credential.rest.model.UserModel;
import com.github.totoCastaldi.services.credential.rest.request.PasswordLostRequest;
import com.github.totoCastaldi.services.credential.rest.request.ResetPasswordRequest;
import com.github.totoCastaldi.services.credential.rest.response.EmptyResponse;
import com.github.totoCastaldi.services.credential.rest.response.PasswordLostResponse;
import com.github.totoCastaldi.services.credential.rest.service.UserChange;
import com.github.totoCastaldi.services.credential.rest.service.UserEmailPasswordLost;
import com.github.totoCastaldi.services.credential.rest.service.UserPasswordLostToken;
import com.google.common.base.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

/**
 * Created by github on 05/12/14.
 */
@Path(ResourcePath.LOST_TOKEN)
@Slf4j
public class PasswordLostResource {

    private final ApiResponse apiResponse;
    private final UserDao userDao;
    private final UserPasswordLostToken userPasswordLostToken;
    private final UserEmailPasswordLost userEmailPasswordLost;
    private final UserChange userChange;
    private final TimeProvider timeProvider;

    @Inject
    public PasswordLostResource(
            ApiResponse apiResponse,
            UserDao userDao,
            UserPasswordLostToken userPasswordLostToken,
            UserEmailPasswordLost userEmailPasswordLost,
            UserChange userChange,
            TimeProvider timeProvider
    ) {
        this.apiResponse = apiResponse;
        this.userDao = userDao;
        this.userPasswordLostToken = userPasswordLostToken;
        this.userEmailPasswordLost = userEmailPasswordLost;
        this.userChange = userChange;
        this.timeProvider = timeProvider;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response passwordLost(
            @Context HttpServletRequest httpServletRequest,
            @NotNull @Valid PasswordLostRequest request
    ) {
        log.info("passwordLost {} ", request);

        final String email = request.getEmail();

        final Optional<UserModel> userModelOptional = userDao.getValidUserByEmail(email);

        if (userModelOptional.isPresent()) {
            final Date now = timeProvider.now();
            final String token = userPasswordLostToken.generateToken(email, now);

            userDao.passwordLostTokenGenerated(email,now);

            boolean proceed = BooleanUtils.isTrue(request.getSkipEmailSend());
            if (!proceed) {
                proceed = userEmailPasswordLost.sendEmail(email, token, request.getBaseUrl());
            }
            if (proceed) {
                return apiResponse.ok(PasswordLostResponse.of(token));
            } else {
                return apiResponse.badResponse("can't send email");
            }

        } else {
            return apiResponse.notFound();
        }
    }

    @PUT
    @Path(ResourcePath.p_P0)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response resetPassword(
            @Context HttpServletRequest httpServletRequest,
            @PathParam(ResourcePath.P0) String token,
            @NotNull @Valid ResetPasswordRequest request
    ) {
        log.info("resetPassword {} {}", token, request);

        final String email = request.getEmail();

        final Optional<UserModel> userModelOptional = userDao.getValidUserByEmail(email);
        if (userModelOptional.isPresent() && userModelOptional.get().getPasswordLostTokenCreation() != null) {
            if (userPasswordLostToken.isCorrect(email, token, userModelOptional.get().getPasswordLostTokenCreation())) {
                if (userDao.updatePassword(email, request.getPassword()).isPresent()) {
                    final UserModel userModel = userModelOptional.get();
                    if (StringUtils.isNotBlank(userModel.getUrlNotifier())) {
                        userChange.notifyExternalService(userModel.getUrlNotifier());
                    }
                    return apiResponse.ok(new EmptyResponse());
                } else {
                    return apiResponse.notFound();
                }
            } else {
                return apiResponse.badResponse(); //wrong token but i'm using a simple error in order to hide info :)
            }

        } else {
            return apiResponse.notFound();
        }

    }

}