package com.abhinav.lagom.unmanged.ComponentTest;

import akka.NotUsed;
import com.abhinav.lagom.unmanaged.api.ComponentsService;
import com.abhinav.lagom.unmanaged.external.api.DigitalOceanService;
import com.abhinav.lagom.unmanaged.external.models.ComponentData;
import com.abhinav.lagom.unmanaged.external.models.Components;
import com.abhinav.lagom.unmanaged.external.models.Page;
import com.abhinav.lagom.unmanaged.models.ComponentsResponse;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.testkit.ServiceTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

public class ComponentsServiceImplCompTest extends Mockito {

    private static final String ID = "1";
    private static final Integer COUNT = 1;
    
    private static List<Components> componentsList = new ArrayList<Components>();
    
    private static Page newPage = new Page() {
        {
            setId(ID);
            setName("");
            setUrl("");
        }
    };
    
    private static Components newComponents = new Components() {
        {
            setStatus("");
            
            setName("");
            
            setCreated_at("");
            
            setUpdated_at("");
            
            setPosition(1);
            
            setDescription("");
            
            setShowcase(false);
            
            setId(ID);
            
            setPage_id("");
            
            setGroup_id("");
            
            setComposite_id("");
            
        }
    };
    
    private static ComponentData newComponentData = new ComponentData() {
        {
            setPage(newPage);
            setComponents(componentsList);
        }
    };
    
    private static ComponentsService service;
    private static ServiceTest.TestServer server;
    private static ServiceTest.Setup setup = defaultSetup()
            .withCassandra(true)
            .withCluster(false)
            .withConfigureBuilder(b -> b.overrides
                    (bind(DigitalOceanService.class).to(ExternalStub.class)));

    @BeforeClass
    public static void setup() {

        server = startServer(setup);
        service = server.client(ComponentsService.class);
    
        componentsList.add(newComponents);

    }

    @AfterClass
    public static void tearDown() {
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    @Test
    public void shouldGetUserData() throws Exception {
        ComponentsResponse receivedResponse = service
                .getComponents(Optional.of(""))
                .invoke()
                .toCompletableFuture().get(10, SECONDS);

        assertEquals("getComponents method fails ", "Total components found was" +COUNT , receivedResponse.getComponents());

    }

    static class ExternalStub implements DigitalOceanService {

        @Override
        public ServiceCall<NotUsed, ComponentData> getComponents() {
            return request -> CompletableFuture.completedFuture(newComponentData);
        }
    }

}
