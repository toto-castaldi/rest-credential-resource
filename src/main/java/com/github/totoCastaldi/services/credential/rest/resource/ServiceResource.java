package com.github.totoCastaldi.services.credential.rest.resource;

import com.github.totoCastaldi.restServer.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by github on 05/12/14.
 */
@Path(ResourcePath.SERVICE)
@Slf4j
public class ServiceResource {

    private final ApiResponse apiResponse;

    @Inject
    public ServiceResource(
            ApiResponse apiResponse
    ) {
        this.apiResponse = apiResponse;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response status(
            @Context HttpServletRequest httpServletRequest
    ) {
        log.info("service status");
        return apiResponse.ok();
    }

}