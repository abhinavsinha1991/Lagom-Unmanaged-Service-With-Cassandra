package com.abhinav.lagom.unmanaged.external.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ComponentData {
    
    @JsonProperty("page")
    public Page page;
    
    @JsonProperty("components")
    public List<Components> components;
    
    public Page getPage() {
        return page;
    }
    
    public void setPage(Page page) {
        this.page = page;
    }
    
    public List<Components> getComponents() {
        return components;
    }
    
    public void setComponents(List<Components> components) {
        this.components = components;
    }
    
    @JsonCreator
    public ComponentData() {
        
    }
    
}
