package com.cuscatlan.coworking.service.impl;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import com.cuscatlan.coworking.common.exception.SpaceNotFoundException;
import com.cuscatlan.coworking.entity.Space;
import com.cuscatlan.coworking.enums.SpaceType;
import com.cuscatlan.coworking.dto.request.space.CreateSpaceRequest;
import com.cuscatlan.coworking.dto.request.space.UpdateSpaceRequest;
import com.cuscatlan.coworking.dto.response.space.SpaceResponse;
import com.cuscatlan.coworking.mapper.SpaceMapper;
import com.cuscatlan.coworking.repository.SpaceRepository;
import com.cuscatlan.coworking.service.SpaceService;
import com.cuscatlan.coworking.specification.SpaceSpecification;

@Service
@RequiredArgsConstructor
@Transactional
public class SpaceServiceImpl implements SpaceService {

    private final SpaceRepository spaceRepository;

    private final SpaceMapper spaceMapper;

    @Override
    public SpaceResponse create(CreateSpaceRequest request) {

        Space space = spaceMapper.toEntity(request);

        Space savedSpace = spaceRepository.save(space);

        return spaceMapper.toResponse(savedSpace);

    }

    @Override
    public SpaceResponse update(
            Long id,
            UpdateSpaceRequest request) {

        Space space = spaceRepository.findById(id)
                .orElseThrow(() -> new SpaceNotFoundException(id));

        spaceMapper.update(request, space);

        Space updated = spaceRepository.save(space);

        return spaceMapper.toResponse(updated);

    }

    @Override
    @Transactional(readOnly = true)
    public SpaceResponse findById(Long id) {

        Space space = spaceRepository.findById(id)
                .orElseThrow(() -> new SpaceNotFoundException(id));

        return spaceMapper.toResponse(space);

    }

    @Override
    @Transactional(readOnly = true)
    public List<SpaceResponse> findAll() {

        return spaceRepository.findAll()
                .stream()
                .map(spaceMapper::toResponse)
                .toList();

    }

    @Override
    @Transactional(readOnly = true)
    public List<SpaceResponse> search(
            Boolean active,
            SpaceType type, Integer capacity) {

        Specification<Space> specification =
                Specification
                        .where(SpaceSpecification.isActive(active))
                        .and(SpaceSpecification.hasType(type)
                        .and(SpaceSpecification.hasCapacity(capacity)));

        return spaceRepository.findAll(specification)
                .stream()
                .map(spaceMapper::toResponse)
                .toList();

    }

}