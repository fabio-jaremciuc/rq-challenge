package com.example.rqchallenge.common.exception;

import com.example.rqchallenge.common.enumer.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestException extends ServiceException {

    public BadRequestException(ErrorInfo errorInfo, Object ... args) {
        super(errorInfo, args);
    }

}
