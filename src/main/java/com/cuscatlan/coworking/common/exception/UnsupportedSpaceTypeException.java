package com.cuscatlan.coworking.common.exception;

import com.cuscatlan.coworking.common.exception.BusinessException;
import com.cuscatlan.coworking.common.exception.ErrorCode;
import com.cuscatlan.coworking.enums.SpaceType;

public class UnsupportedSpaceTypeException extends BusinessException {

    public UnsupportedSpaceTypeException(SpaceType type) {

        super(
                ErrorCode.UNSUPPORTED_SPACE_TYPE,
                "No pricing strategy registered for space type: " + type
        );

    }

}