package com.github.totoCastaldi.services.credential.rest.service;

import com.github.totoCastaldi.restServer.ApiPassword;
import com.github.totoCastaldi.restServer.TimeProvider;
import com.github.totoCastaldi.services.credential.rest.Conf;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by toto on 17/11/15.
 */
public class UserPasswordLostToken extends UserToken {

    @Inject
    public UserPasswordLostToken(
            @Named(Conf.PASSWORD_LOST_TOKEN_SEED) String seed
    ) {
        super(seed, "UserPasswordLostToken");
    }
}
