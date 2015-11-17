package com.github.totoCastaldi.services.credential.rest;

import lombok.Getter;

/**
 * Created by toto on 16/11/15.
 */

public enum ERROR {
    CANT_SEND_EMAIL ("can't send email", "can't send email");
    @Getter
    private final String code;
    @Getter
    private final String description;

    ERROR (String code, String description) {
        this.code = code;
        this.description = description;
    }
}
