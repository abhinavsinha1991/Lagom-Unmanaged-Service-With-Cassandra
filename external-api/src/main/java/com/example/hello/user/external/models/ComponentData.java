package com.example.hello.user.external.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ComponentData {
    
    @JsonProperty("page")
    public Page page;
    
    @JsonProperty("components")
    public List<Components> components;
    
    @JsonCreator
    public ComponentData() {
        
    }
    
}
