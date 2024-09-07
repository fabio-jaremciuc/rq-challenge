package com.example.rqchallenge.spring;

import com.example.rqchallenge.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Configuration
public class ServiceErrorAttributes extends DefaultErrorAttributes {

    private static final String MESSAGE_TAG_NAME = "message";

    private static final Logger LOG = LoggerFactory.getLogger(ServiceErrorAttributes.class);

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);

        Throwable throwable = getError(webRequest);

        errorAttributes.put("id",  UUID.randomUUID().toString());

        errorAttributes.put("timestamp", new Date());

        if (throwable instanceof ServiceException) {
            ServiceException serviceException = (ServiceException) throwable;

            // Error Description message added
            errorAttributes.put(MESSAGE_TAG_NAME, serviceException.getMessage());

            // Internal error code added
            errorAttributes.put("errorCode", serviceException.getErrorCode());
        } else {
            parentError(webRequest, errorAttributes);
        }

        LOG.error("An exception was thrown: {}", errorAttributes);
        return errorAttributes;
    }

    private void parentError(WebRequest webRequest, Map<String, Object> errorAttributes) {
        if (super.getError(webRequest) != null) {
            errorAttributes.put(MESSAGE_TAG_NAME, super.getError(webRequest).getMessage());
        }
    }

}
