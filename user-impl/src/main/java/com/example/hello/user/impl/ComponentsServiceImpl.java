package com.example.hello.user.impl;

import akka.NotUsed;
import com.example.hello.user.api.ComponentsService;
import com.example.hello.user.external.api.DigitalOceanService;
import com.example.hello.user.external.models.ComponentData;
import com.example.hello.user.external.models.Components;
import com.example.hello.user.mapper.ResponseMapper;
import com.example.hello.user.models.ComponentsResponse;
import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;


public class ComponentsServiceImpl implements ComponentsService {
    
    private DigitalOceanService digitalOceanService;
    
    private final ResponseMapper responseMapper;
    
    private CassandraSession cassandraSession;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentsServiceImpl.class);
    
    private static final List<String> validStatusList = Arrays.asList("operational", "degraded_performance", "partial_outage", "major_outage");
    
    @Inject
    public ComponentsServiceImpl(DigitalOceanService digitalOceanService, ResponseMapper responseMapper, CassandraSession cassandraSession) {
        this.digitalOceanService = digitalOceanService;
        this.responseMapper = responseMapper;
        this.cassandraSession = cassandraSession;
    }
    
    public CompletionStage<ComponentData> hitAPI() {
        CompletionStage<ComponentData> componentDataFromDoApi = digitalOceanService
                .getUser()
                .invoke();
        return componentDataFromDoApi;
        
    }
    
    @Override
    public ServiceCall<NotUsed, ComponentsResponse> getComponents(Optional<String> name) {
        return request ->
        {
            CompletionStage<ComponentData> componentData = hitAPI();
            
            String name1 = name.orElse("");
            
            LOGGER.info("Query parameter received is {}", name1);
            
            String[] arr = name1.split(",");
            
            List<String> namelist = Arrays.asList(arr);
            
            return componentData.thenApply(
                    
                    userInfo ->
                    
                    {
                        cassandraSession.underlying().thenAccept(a-> System.out.println(a.getLoggedKeyspace()));
                        
                        LOGGER.info("Total no. of components fetched from external service is: {} ", userInfo.components.size());
                        
                        LOGGER.info("List of valid status is" + validStatusList);
                        
                        List<Components> componentsList;
                        
                        Stream<Components> componentsStream = userInfo.components.stream()
                                .filter(b -> b.getGroup_id() != null)
                                .filter(c -> validStatusList.stream().anyMatch(d -> d.equals(c.getStatus())))
                                .map(d-> {
                                    String newString= d.getGroup_id().concat(d.getPage_id());
                                    d.setComposite_id(Integer.toString(newString.hashCode()));
                                    return d;
                                });
                        
                        if (!name1.equals("")) {
                            componentsList = componentsStream
                                    .filter(a -> namelist.contains(a.getName()))
                                    .collect(Collectors.toList());
                        } else {
                            componentsList = componentsStream.collect(Collectors.toList());
                        }
                        
                        LOGGER.info("Filtered list size of components is: {} ", componentsList.size());
                        
                        try {
                            
                            cassandraSession.selectAll("select * from comp_data.comps").thenAccept(a-> System.out.println(a.size()));
                            componentsList.stream().forEach(a -> cassandraSession.executeWrite("INSERT INTO comp_data.comps" +
                                            "(name, status, composite_id)" +
                                            "VALUES(?, ?, ?)",a.getName(),a.getStatus(), a.getComposite_id()));
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ComponentsResponse userResponse = responseMapper.getResponse(componentsList);
                        
                        return userResponse;
                    }
            );
        };
    }
    
}
