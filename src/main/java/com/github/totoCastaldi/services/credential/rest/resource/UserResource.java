package com.github.totoCastaldi.services.credential.rest.resource;

import com.github.totoCastaldi.restServer.response.ApiResponse;
import com.github.totoCastaldi.services.credential.rest.model.UserDao;
import com.github.totoCastaldi.services.credential.rest.model.UserModel;
import com.github.totoCastaldi.services.credential.rest.request.CreateUserRequest;
import com.github.totoCastaldi.services.credential.rest.service.UserMailActivation;
import com.google.common.base.Optional;
import lombok.extern.slf4j.Slf4j;

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
    private final UserMailActivation userMailActivation;

    @Inject
    public UserResource(
            ApiResponse apiResponse,
            UserDao userDao,
            UserMailActivation userMailActivation
    ) {
        this.apiResponse = apiResponse;
        this.userDao = userDao;
        this.userMailActivation = userMailActivation;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response on(
            @Context HttpServletRequest httpServletRequest,
            @NotNull @Valid CreateUserRequest request
    ) {
        Optional<UserModel> userModelOptional = userDao.create(request.getEmail(), request.getPassword());
        if (userModelOptional.isPresent()) {
            if (userMailActivation.sendEmail(userModelOptional.get().getEmail())) {
                return apiResponse.createdReturns(httpServletRequest, ResourcePath.USER, String.valueOf(userModelOptional.get().getId()));
            } else {
                return apiResponse.badResponse("can't send email");
            }
        } else {
            //already exist but return a generic error in order to hide data
            return apiResponse.badResponse();
        }
    }

}