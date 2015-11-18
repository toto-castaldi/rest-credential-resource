package com.github.totoCastaldi.services.credential.rest.resource;

import com.github.totoCastaldi.restServer.response.ApiResponse;
import com.github.totoCastaldi.services.credential.rest.model.UserDao;
import com.github.totoCastaldi.services.credential.rest.model.UserModel;
import com.github.totoCastaldi.services.credential.rest.request.ValidateTokenRequest;
import com.github.totoCastaldi.services.credential.rest.service.UserConfirmToken;
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
@Path(ResourcePath.CONFIR_TOKEN)
@Slf4j
public class ConfirmTokenResource {

    private final ApiResponse apiResponse;
    private final UserDao userDao;
    private final UserConfirmToken userConfirmToken;

    @Inject
    public ConfirmTokenResource(
            ApiResponse apiResponse,
            UserDao userDao,
            UserConfirmToken userConfirmToken
    ) {
        this.apiResponse = apiResponse;
        this.userDao = userDao;
        this.userConfirmToken = userConfirmToken;
    }

    @PUT
    @Path(ResourcePath.p_P0)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response validateToken(
            @Context HttpServletRequest httpServletRequest,
            @PathParam(ResourcePath.P0) String token,
            @NotNull @Valid ValidateTokenRequest request
    ) {
        log.info("validateToken {} {}", token, request);

        final String email = request.getEmail();
        final Optional<UserModel> userModelOptional = userDao.getByEmail(email);

        if (userModelOptional.isPresent()) {
            if (userConfirmToken.isCorrect(email, token)) {
                final Optional<UserModel> confirmedOptional = userDao.confirmed(email);
                if (confirmedOptional.isPresent()) {
                    return apiResponse.ok();
                } else {
                    return apiResponse.notFound();
                }
            } else {
                return apiResponse.badResponse("wrong token or email");
            }
        } else {
            return apiResponse.notFound();
        }
    }

}