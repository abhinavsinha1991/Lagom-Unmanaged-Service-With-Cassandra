package com.abhinav.lagom.unmanaged.api;

import akka.NotUsed;
import com.abhinav.lagom.unmanaged.models.ComponentsResponse;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;

import java.util.Optional;

import static com.lightbend.lagom.javadsl.api.Service.restCall;
import static com.lightbend.lagom.javadsl.api.Service.named;

public interface ComponentsService extends Service{

    ServiceCall<NotUsed, ComponentsResponse> getComponents(Optional<String> name);

    @Override
    default Descriptor descriptor(){
        return named("helloUser").withCalls(
                restCall(Method.GET,"/api/v1/components?name",this::getComponents)
                ).withAutoAcl(true);
    }

}
