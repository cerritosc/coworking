package com.cuscatlan.coworking.specification;

import org.springframework.data.jpa.domain.Specification;

import com.cuscatlan.coworking.entity.Space;
import com.cuscatlan.coworking.enums.SpaceType;

public final class SpaceSpecification {

    private SpaceSpecification() {
    }

    public static Specification<Space> hasType(SpaceType type) {

        return (root, query, criteriaBuilder) ->
                type == null
                        ? null
                        : criteriaBuilder.equal(root.get("type"), type);

    }

    public static Specification<Space> isActive(Boolean active) {

        return (root, query, criteriaBuilder) ->
                active == null
                        ? null
                        : criteriaBuilder.equal(root.get("active"), active);

    }
    
    public static Specification<Space> hasCapacity(Integer capacity) {

        return (root, query, cb) ->
                capacity == null
                        ? null
                        : cb.greaterThanOrEqualTo(
                                root.get("capacity"),
                                capacity);

    }

}