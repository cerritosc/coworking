package com.cuscatlan.coworking.common.exception;

public class SpaceInactiveException extends BusinessException {

    public SpaceInactiveException() {
        super(ErrorCode.SPACE_INACTIVE);
    }

    public SpaceInactiveException(Long spaceId) {
        super(
                ErrorCode.SPACE_INACTIVE,
                "Space with id %d is inactive.".formatted(spaceId)
        );
    }

}