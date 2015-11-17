package com.github.totoCastaldi.services.credential.rest.service;

import com.github.totoCastaldi.restServer.ApiPassword;
import com.github.totoCastaldi.services.credential.rest.Conf;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by toto on 16/11/15.
 */
public class UserPassword extends ApiPassword {

    @Inject
    public UserPassword(
            @Named(Conf.PASSWORD_SEED) String seed
    ) {
        super(seed);
    }
}
