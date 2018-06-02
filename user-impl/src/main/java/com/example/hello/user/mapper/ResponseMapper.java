package com.example.hello.user.mapper;

import com.example.hello.user.external.models.Components;
import com.example.hello.user.models.ComponentsResponse;

import java.util.List;

public class ResponseMapper {

    public ComponentsResponse getResponse(List<Components> userData) {
        ComponentsResponse userResponse = new ComponentsResponse();
        userResponse.setComponents("Total components found was " + userData.size());
        return userResponse;
    }
}
