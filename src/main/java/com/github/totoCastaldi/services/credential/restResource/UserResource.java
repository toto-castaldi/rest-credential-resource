package com.github.totoCastaldi.services.credential.restResource;

import com.github.totoCastaldi.restServer.annotation.BasicAuthenticated;
import com.github.totoCastaldi.restServer.annotation.UserProfileCustomer;
import com.github.totoCastaldi.restServer.response.ApiResponse;
import com.google.common.base.Optional;
import lombok.extern.slf4j.Slf4j;

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

    @Inject
    public UserResource(
            ApiResponse apiResponse,
            UserDao userDao
    ) {
        this.apiResponse = apiResponse;
        this.userDao = userDao;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @BasicAuthenticated
    @UserProfileCustomer
    public Response on(
            @Context HttpServletRequest httpServletRequest,
            @NotNull @Valid CreateUserRequest request
    ) {
        Optional<Long> longOptional = userDao.create(request.getEmail(), request.getPassword());
        if (longOptional.isPresent()) {
            return apiResponse.createdReturns(httpServletRequest, ResourcePath.USER, String.valueOf(longOptional.get()));
        } else {
            return apiResponse.badResponse();
        }
    }

}