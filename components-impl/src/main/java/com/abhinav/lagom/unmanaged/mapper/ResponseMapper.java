package com.abhinav.lagom.unmanaged.mapper;

import com.abhinav.lagom.unmanaged.external.models.Components;
import com.abhinav.lagom.unmanaged.models.ComponentsResponse;

import java.util.List;

public class ResponseMapper {

    public ComponentsResponse getResponse(List<Components> userData) {
        ComponentsResponse userResponse = new ComponentsResponse();
        userResponse.setComponents("Total components found was " + userData.size());
        return userResponse;
    }
}
