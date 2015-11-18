package com.github.totoCastaldi.services.credential.rest.resource;

import com.github.totoCastaldi.restServer.response.ApiResponse;
import com.github.totoCastaldi.services.credential.rest.model.UserDao;
import com.github.totoCastaldi.services.credential.rest.model.UserModel;
import com.github.totoCastaldi.services.credential.rest.request.PasswordLostRequest;
import com.github.totoCastaldi.services.credential.rest.response.PasswordLostResponse;
import com.github.totoCastaldi.services.credential.rest.service.UserEmailPasswordLost;
import com.github.totoCastaldi.services.credential.rest.service.UserPasswordLostToken;
import com.google.common.base.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by github on 05/12/14.
 */
@Path(ResourcePath.PASSWORD_LOST)
@Slf4j
public class PasswordLostResource {

    private final ApiResponse apiResponse;
    private final UserDao userDao;
    private final UserPasswordLostToken userPasswordLostToken;
    private final UserEmailPasswordLost userEmailPasswordLost;

    @Inject
    public PasswordLostResource(
            ApiResponse apiResponse,
            UserDao userDao,
            UserPasswordLostToken userPasswordLostToken,
            UserEmailPasswordLost userEmailPasswordLost
    ) {
        this.apiResponse = apiResponse;
        this.userDao = userDao;
        this.userPasswordLostToken = userPasswordLostToken;
        this.userEmailPasswordLost = userEmailPasswordLost;
    }

    @PUT
    @Path(ResourcePath.p_P0)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response passwordLost(
            @Context HttpServletRequest httpServletRequest,
            @PathParam(ResourcePath.P0) String email,
            @NotNull @Valid PasswordLostRequest request
    ) {
        log.info("passwordLost {} {}", email, request);

        final Optional<UserModel> userModelOptional = userDao.getByEmail(email);

        if (userModelOptional.isPresent()) {
            final String token = userPasswordLostToken.generateToken(email);

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

}