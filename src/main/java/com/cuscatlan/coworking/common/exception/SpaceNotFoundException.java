package com.cuscatlan.coworking.common.exception;

public class SpaceNotFoundException extends BusinessException {

    public SpaceNotFoundException(Long id) {

        super(
                ErrorCode.SPACE_NOT_FOUND,
                "Space with id %d was not found.".formatted(id)
        );

    }

}