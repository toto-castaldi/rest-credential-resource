package com.github.totoCastaldi.services.credential.restResource;

import com.github.totoCastaldi.restServer.ApiServletContextListener;
import com.github.totoCastaldi.restServer.RestServerConf;
import com.github.totoCastaldi.restServer.plugin.PersistenModule;
import com.google.inject.AbstractModule;

public class ContextListener extends ApiServletContextListener {

    @Override
    public RestServerConf getAppConf() {
        RestServerConf.Builder builder = RestServerConf.builder();
        builder.add(UserResource.class.getPackage());
        builder.add(new AbstractModule() {
            @Override
            protected void configure() {
                bind(ExampleResourceSupport.class);
                bind(UserDao.class);
            }
        });
        builder.add(new PersistenModule());
        return builder.build();
    }
}
