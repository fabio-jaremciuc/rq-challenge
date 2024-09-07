package com.example.rqchallenge.common.exception;

import com.example.rqchallenge.common.enumer.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends ServiceException {

    public NotFoundException(ErrorInfo errorInfo, Object ... args) {
        super(errorInfo, args);
    }

}
