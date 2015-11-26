package com.github.totoCastaldi.services.credential.rest.resource;

import com.github.totoCastaldi.restServer.TimeProvider;
import com.github.totoCastaldi.restServer.response.ApiResponse;
import com.github.totoCastaldi.services.credential.rest.model.UserDao;
import com.github.totoCastaldi.services.credential.rest.model.UserModel;
import com.github.totoCastaldi.services.credential.rest.request.ChangePasswordRequest;
import com.github.totoCastaldi.services.credential.rest.request.CreateUserRequest;
import com.github.totoCastaldi.services.credential.rest.request.DeleteUserRequest;
import com.github.totoCastaldi.services.credential.rest.response.CreateUserReponse;
import com.github.totoCastaldi.services.credential.rest.response.EmptyResponse;
import com.github.totoCastaldi.services.credential.rest.service.UserChange;
import com.github.totoCastaldi.services.credential.rest.service.UserConfirmToken;
import com.github.totoCastaldi.services.credential.rest.service.UserEmailActivation;
import com.github.totoCastaldi.services.credential.rest.service.UserPassword;
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
@Path(ResourcePath.USER)
@Slf4j
public class UserResource {

    private final ApiResponse apiResponse;
    private final UserDao userDao;
    private final UserEmailActivation userEmailActivation;
    private final UserConfirmToken userConfirmToken;
    private final UserPassword userPassword;
    private final UserChange userChange;
    private final TimeProvider timeProvider;

    @Inject
    public UserResource(
            ApiResponse apiResponse,
            UserDao userDao,
            UserEmailActivation userEmailActivation,
            UserConfirmToken userConfirmToken,
            UserPassword userPassword,
            UserChange userChange,
            TimeProvider timeProvider
    ) {
        this.apiResponse = apiResponse;
        this.userDao = userDao;
        this.userEmailActivation = userEmailActivation;
        this.userConfirmToken = userConfirmToken;
        this.userPassword = userPassword;
        this.userChange = userChange;
        this.timeProvider = timeProvider;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(
            @Context HttpServletRequest httpServletRequest,
            @NotNull @Valid CreateUserRequest request
    ) {
        Optional<UserModel> userCreationOptional = userDao.create(request.getEmail(), request.getPassword(), request.getUrlNotifier());
        if (userCreationOptional.isPresent()) {
            final UserModel userModel = userCreationOptional.get();
            final Date now = timeProvider.now();
            final String email = userModel.getEmail();
            final String token = userConfirmToken.generateToken(email, now);
            userDao.confirmTokenGenerated(email, now);
            boolean proceed = BooleanUtils.isTrue(request.getSkipEmailSend());
            if (!proceed) {
                proceed = userEmailActivation.sendEmail(email, token, request.getUrlBaseConfirm());
            }
            if (proceed) {
                CreateUserReponse createUserReponse = CreateUserReponse.of(token);
                return apiResponse.createdReturns(httpServletRequest, createUserReponse, ResourcePath.USER, String.valueOf(userModel.getId()));
            } else {
                return apiResponse.badResponse("can't send email");
            }
        } else {
            //already exist but return a generic error in order to hide data
            return apiResponse.badResponse();
        }
    }

    @GET
    @Path(ResourcePath.USER_CREDENTIAL)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(
            @Context HttpServletRequest httpServletRequest,
            @PathParam(ResourcePath.P0) String email,
            @PathParam(ResourcePath.P1) String password
    ) {
        log.info("login for {} {}", email, password);
        final Optional<UserModel> validUserByEmail = userDao.getValidUserByEmail(email);
        if (validUserByEmail.isPresent()) {
            final UserModel userModel = validUserByEmail.get();

            if (userPassword.validate(email, password, userModel.getEncodedPassword())) {
                return apiResponse.ok(new EmptyResponse());
            } else {
                return apiResponse.notFound();
            }
        } else {
            return apiResponse.notFound();
        }
    }

    @PUT
    @Path(ResourcePath.p_P0)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changePassword(
            @Context HttpServletRequest httpServletRequest,
            @PathParam(ResourcePath.P0) String email,
            @NotNull @Valid ChangePasswordRequest request
    ) {
        log.info("change password {} {}", email, request);
        final Optional<UserModel> validUserByEmail = userDao.getValidUserByEmail(email);
        if (validUserByEmail.isPresent()) {
            final UserModel userModel = validUserByEmail.get();

            if (userPassword.validate(email, request.getOldPassword(), userModel.getEncodedPassword())) {
                if (userDao.updatePassword(email, request.getNewPassword()).isPresent()) {
                    return apiResponse.ok(new EmptyResponse());
                } else {
                    return apiResponse.notFound();
                }
            } else {
                return apiResponse.notFound();
            }
        } else {
            return apiResponse.notFound();
        }
    }

    @DELETE
    @Path(ResourcePath.p_P0)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(
            @Context HttpServletRequest httpServletRequest,
            @PathParam(ResourcePath.P0) String email,
            @NotNull @Valid DeleteUserRequest request
    ) {
        log.info("delete user {} {}", email, request);
        Optional<UserModel> userModelOptional = userDao.getValidUserByEmail(email);
        if (userModelOptional.isPresent()) {
            final UserModel userModel = userModelOptional.get();
            if (userPassword.validate(email, request.getPassword(), userModel.getEncodedPassword())) {
                if (userDao.delete(email).isPresent()) {
                    if (StringUtils.isNotBlank(userModel.getUrlNotifier())) {
                        userChange.notifyExternalService(userModel.getUrlNotifier());
                    }
                    return apiResponse.ok(new EmptyResponse());
                } else {
                    return apiResponse.notFound();
                }
            } else {
                return apiResponse.badResponse();
            }
        } else {
            //already does not exist but return a generic error in order to hide data
            return apiResponse.badResponse();
        }
    }

}