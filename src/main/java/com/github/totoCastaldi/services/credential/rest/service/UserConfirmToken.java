package com.github.totoCastaldi.services.credential.rest.service;

import com.github.totoCastaldi.restServer.ApiPassword;
import com.github.totoCastaldi.services.credential.rest.Conf;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by toto on 17/11/15.
 */
public class UserConfirmToken extends ApiPassword {

    private static final String PWD = "UserConfirmToken";

    @Inject
    public UserConfirmToken(
            @Named(Conf.CONFIRM_TOKEN_SEED) String seed
    ) {
        super(seed);
    }

    public String generateToken(String email) {
        return super.encodePassword(email, PWD);
    }

    public boolean isCorrect(String email, String token) {
        return super.validate(email, PWD, token);
    }
}
