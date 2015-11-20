package com.github.totoCastaldi.services.credential.rest.resource;

import com.github.totoCastaldi.restServer.response.ApiResponse;
import com.github.totoCastaldi.services.credential.rest.model.UserDao;
import com.github.totoCastaldi.services.credential.rest.model.UserModel;
import com.github.totoCastaldi.services.credential.rest.request.CreateUserRequest;
import com.github.totoCastaldi.services.credential.rest.request.DeleteUserRequest;
import com.github.totoCastaldi.services.credential.rest.response.CreateUserReponse;
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

    @Inject
    public UserResource(
            ApiResponse apiResponse,
            UserDao userDao,
            UserEmailActivation userEmailActivation,
            UserConfirmToken userConfirmToken,
            UserPassword userPassword,
            UserChange userChange
    ) {
        this.apiResponse = apiResponse;
        this.userDao = userDao;
        this.userEmailActivation = userEmailActivation;
        this.userConfirmToken = userConfirmToken;
        this.userPassword = userPassword;
        this.userChange = userChange;
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
            final String token = userConfirmToken.generateToken(userModel.getEmail());
            boolean proceed = BooleanUtils.isTrue(request.getSkipEmailSend());
            if (!proceed) {
                proceed = userEmailActivation.sendEmail(userModel.getEmail(), token, request.getUrlBaseConfirm());
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
                    return apiResponse.ok();
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