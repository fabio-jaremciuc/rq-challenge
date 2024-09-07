package com.example.rqchallenge.common.exception;

import com.example.rqchallenge.common.enumer.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class RemoteApiException extends ServiceException {

    public RemoteApiException(ErrorInfo errorInfo, Object... args) {
        super(errorInfo, args);
    }
}
