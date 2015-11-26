package com.github.totoCastaldi.services.credential.rest.service;

import com.github.totoCastaldi.restServer.ApiPassword;

import java.util.Date;

/**
 * Created by toto on 18/11/15.
 */
public abstract class UserToken extends ApiPassword {

    private final String pwd;

    public UserToken(String seed, String pwd) {
        super(seed);
        this.pwd = pwd;
    }

    public String generateToken(String email, Date date) {
        return super.encodePassword(withDate(email, date), pwd);
    }

    private String withDate(String email, Date date) {
        return email  + String.valueOf(date.getTime());
    }

    public boolean isCorrect(String email, String token, Date date) {
        return super.validate(withDate(email, date), pwd, token);
    }
}
