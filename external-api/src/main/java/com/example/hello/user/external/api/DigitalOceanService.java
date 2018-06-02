package com.example.hello.user.external.api;

import akka.NotUsed;
import com.example.hello.user.external.models.ComponentData;
import com.example.hello.user.external.models.UserData;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;

public interface DigitalOceanService extends Service {

    ServiceCall<NotUsed, ComponentData> getUser();

    @Override
    default Descriptor descriptor() {
        return Service.named("external-service").withCalls(
                Service.restCall(Method.GET, "/api/v1/components.json", this::getUser)

        ).withAutoAcl(true);
    }

}
