package com.github.totoCastaldi.services.credential.restResource;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;

/**
 * Created by github on 08/11/15.
 */
@Slf4j
public class ExampleHeaderCheckFilter implements ContainerRequestFilter {

    @Inject
    public ExampleHeaderCheckFilter(
    ) {

    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String authorizationHeader = containerRequestContext.getHeaderString("AA");

        log.info("ExampleHeaderCheckFilter {}", authorizationHeader);
    }
}

