package com.abhinav.lagom.unmanaged.impl;

import com.abhinav.lagom.unmanaged.external.api.DigitalOceanService;
import com.abhinav.lagom.unmanaged.api.ComponentsService;
import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import play.Configuration;
import play.Environment;

public class ComponentsModule extends AbstractModule implements ServiceGuiceSupport {

    private final Environment environment;
    private final Configuration configuration;  //NOSONAR as this is required field.

    public ComponentsModule(Environment environment, Configuration configuration) {
        this.environment = environment;
        this.configuration = configuration;
    }

    @Override
    protected void configure() {
        bindService(ComponentsService.class, ComponentsServiceImpl.class);
        bindClient(DigitalOceanService.class);

    }

}
