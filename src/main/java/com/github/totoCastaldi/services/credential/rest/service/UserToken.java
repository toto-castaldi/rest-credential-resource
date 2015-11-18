package com.github.totoCastaldi.services.credential.rest.service;

import com.github.totoCastaldi.restServer.ApiPassword;

/**
 * Created by toto on 18/11/15.
 */
public class UserToken extends ApiPassword {

    private final String pwd;

    public UserToken(String seed, String pwd) {
        super(seed);
        this.pwd = pwd;
    }

    public String generateToken(String email) {
        return super.encodePassword(email, pwd);
    }

    public boolean isCorrect(String email, String token) {
        return super.validate(email, pwd, token);
    }
}
