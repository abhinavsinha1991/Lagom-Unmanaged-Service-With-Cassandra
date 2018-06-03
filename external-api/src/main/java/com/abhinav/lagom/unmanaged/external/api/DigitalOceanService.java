package com.abhinav.lagom.unmanaged.external.api;

import akka.NotUsed;
import com.abhinav.lagom.unmanaged.external.models.ComponentData;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;

public interface DigitalOceanService extends Service {

    ServiceCall<NotUsed, ComponentData> getComponents();

    @Override
    default Descriptor descriptor() {
        return Service.named("external-service").withCalls(
                Service.restCall(Method.GET, "/api/v1/components.json", this::getComponents)

        ).withAutoAcl(true);
    }

}
