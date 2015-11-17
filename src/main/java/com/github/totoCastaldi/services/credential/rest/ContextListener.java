package com.github.totoCastaldi.services.credential.rest;

import com.github.totoCastaldi.restServer.ApiServletContextListener;
import com.github.totoCastaldi.restServer.RestServerConf;
import com.github.totoCastaldi.restServer.plugin.PersistenModule;
import com.github.totoCastaldi.services.credential.rest.model.UserDao;
import com.github.totoCastaldi.services.credential.rest.resource.UserResource;
import com.github.totoCastaldi.services.credential.rest.service.UserMailActivation;
import com.github.totoCastaldi.services.credential.rest.service.UserPassword;
import com.google.inject.AbstractModule;

public class ContextListener extends ApiServletContextListener {

    @Override
    public RestServerConf getAppConf() {
        RestServerConf.Builder builder = RestServerConf.builder();
        builder.add(UserResource.class.getPackage());
        builder.add(new AbstractModule() {
            @Override
            protected void configure() {
                bind(UserDao.class);
                bind(UserMailActivation.class);
                bind(UserPassword.class);
            }
        });
        builder.add(new PersistenModule());
        builder.addStringConf(Conf.PASSWORD_SEED);
        builder.addStringConf(Conf.MAIL_USERNAME);
        builder.addStringConf(Conf.MAIL_PASSWORD);
        builder.addStringConf(Conf.MAIL_ADDRESS);
        return builder.build();
    }
}
