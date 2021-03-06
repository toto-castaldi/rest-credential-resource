package com.github.totoCastaldi.services.credential.rest.service;

import com.github.totoCastaldi.services.credential.rest.Conf;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by toto on 17/11/15.
 */
public class UserConfirmToken extends UserToken {

    @Inject
    public UserConfirmToken(
            @Named(Conf.CONFIRM_TOKEN_SEED) String seed
    ) {
        super(seed, "UserConfirmToken");
    }

}
