package com.example.rqchallenge.common.exception;

import com.example.rqchallenge.common.enumer.ErrorInfo;

public abstract class ServiceException extends RuntimeException {

    private final Integer errorCode;

    public Integer getErrorCode() {
        return errorCode;
    }

    protected ServiceException(ErrorInfo errorInfo, Object ... args) {
        super(errorInfo.getMessage(args));
        this.errorCode = errorInfo.getErrorCode();
    }
}
