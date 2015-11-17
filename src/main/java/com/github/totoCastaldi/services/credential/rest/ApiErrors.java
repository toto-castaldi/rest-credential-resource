package com.github.totoCastaldi.services.credential.rest;

import com.github.totoCastaldi.restServer.response.ErrorResponse;

/**
 * Created by toto on 16/11/15.
 */
public class ApiErrors {

    public ErrorResponse cantSendEmail() {
        return ErrorResponse.of(ERROR.CANT_SEND_EMAIL.getCode(), ERROR.CANT_SEND_EMAIL.getDescription());
    }

}
