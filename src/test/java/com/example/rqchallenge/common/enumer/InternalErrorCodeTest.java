package com.example.rqchallenge.common.enumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Internal Error Code Test")
class InternalErrorCodeTest {

    @Test
    void test() {

        HashSet<Integer> errorCodes = new HashSet<>();
        HashSet<String> messages = new HashSet<>();

        Object[] getMessageArgs = new Object[100];

        for (ErrorInfo internalErrorCode : ErrorInfo.values()) {
            assertNotNull(internalErrorCode.getErrorCode(), String.format("%s's error code should not be null", internalErrorCode));

            assertNotNull(String.format("%s's message should not be null", internalErrorCode), internalErrorCode.getMessage(getMessageArgs));
            assertFalse(internalErrorCode.getMessage(getMessageArgs).isEmpty(), String.format("%s's message should not be empty", internalErrorCode));

            assertFalse(errorCodes.contains(internalErrorCode.getErrorCode()), String.format("The error code %d is duplicated", internalErrorCode.getErrorCode()));
            errorCodes.add(internalErrorCode.getErrorCode());

            assertFalse(messages.contains(internalErrorCode.getMessage(getMessageArgs)), String.format("The error message \"%s\" is duplicated", internalErrorCode.getMessage(getMessageArgs)));
            messages.add(internalErrorCode.getMessage(getMessageArgs));
        }
    }

}