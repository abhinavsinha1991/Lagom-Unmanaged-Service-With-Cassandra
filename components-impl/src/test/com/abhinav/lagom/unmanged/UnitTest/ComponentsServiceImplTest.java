package com.abhinav.lagom.unmanged.UnitTest;

import akka.NotUsed;
import com.abhinav.lagom.unmanaged.external.api.DigitalOceanService;
import com.abhinav.lagom.unmanaged.external.models.ComponentData;
import com.abhinav.lagom.unmanaged.external.models.Components;
import com.abhinav.lagom.unmanaged.external.models.Page;
import com.abhinav.lagom.unmanaged.impl.ComponentsServiceImpl;
import com.abhinav.lagom.unmanaged.mapper.ResponseMapper;
import com.abhinav.lagom.unmanaged.models.ComponentsResponse;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

public class ComponentsServiceImplTest {

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

    @SuppressWarnings("unused")
    @Tested
    private ComponentsServiceImpl componentServiceImpl;

    @SuppressWarnings("unused")
    @Injectable
    private DigitalOceanService digitalOceanService;

    @SuppressWarnings("unused")
    @Injectable
    private ResponseMapper responseMapper;

    @Test
    public void getComponentsTest() throws Exception {
        new Expectations() {
            {
                digitalOceanService.getComponents();

                result = new ServiceCall<NotUsed, ComponentData>() {
                    @Override
                    public CompletionStage<ComponentData> invoke(NotUsed notUsed) {
                        return CompletableFuture.completedFuture(newComponentData);
                    }
                };
            }

            {
                responseMapper.getResponse(componentsList);
                
                result = new ComponentsResponse() {
                    {
                        setComponents("Total components found was " + newComponentData.getComponents().size());
                    }
                };
            }
        };
        
        ComponentsResponse receivedResponse = componentServiceImpl
                .getComponents(Optional.of(""))
                .invoke()
                .toCompletableFuture().get(5, SECONDS);

        System.out.println(receivedResponse);
        assertEquals("getComponents method fails ", "Total components found was" +COUNT , receivedResponse.getComponents());
    }

}
