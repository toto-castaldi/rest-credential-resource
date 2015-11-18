package com.github.totoCastaldi.services.credential.rest.resource;

import com.github.totoCastaldi.restServer.response.ApiResponse;
import com.github.totoCastaldi.services.credential.rest.model.UserDao;
import com.github.totoCastaldi.services.credential.rest.model.UserModel;
import com.github.totoCastaldi.services.credential.rest.request.CreateUserRequest;
import com.github.totoCastaldi.services.credential.rest.response.CreateUserReponse;
import com.github.totoCastaldi.services.credential.rest.service.UserConfirmToken;
import com.github.totoCastaldi.services.credential.rest.service.UserEmailActivation;
import com.google.common.base.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

    @Inject
    public UserResource(
            ApiResponse apiResponse,
            UserDao userDao,
            UserEmailActivation userEmailActivation,
            UserConfirmToken userConfirmToken
    ) {
        this.apiResponse = apiResponse;
        this.userDao = userDao;
        this.userEmailActivation = userEmailActivation;
        this.userConfirmToken = userConfirmToken;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response on(
            @Context HttpServletRequest httpServletRequest,
            @NotNull @Valid CreateUserRequest request
    ) {
        Optional<UserModel> userModelOptional = userDao.create(request.getEmail(), request.getPassword(), request.getUrlNotifier());
        if (userModelOptional.isPresent()) {
            final UserModel userModel = userModelOptional.get();
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

}