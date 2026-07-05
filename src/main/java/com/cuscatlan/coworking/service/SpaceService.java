package com.cuscatlan.coworking.service;

import java.util.List;

import com.cuscatlan.coworking.dto.request.space.CreateSpaceRequest;
import com.cuscatlan.coworking.dto.request.space.UpdateSpaceRequest;
import com.cuscatlan.coworking.dto.response.space.SpaceResponse;
import com.cuscatlan.coworking.enums.SpaceType;

public interface SpaceService {

    SpaceResponse create(CreateSpaceRequest request);

    SpaceResponse update(Long id, UpdateSpaceRequest request);

    SpaceResponse findById(Long id);

    List<SpaceResponse> findAll();

    List<SpaceResponse> search(Boolean active, SpaceType type, Integer capacity);

}