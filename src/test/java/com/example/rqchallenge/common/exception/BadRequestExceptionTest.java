package com.example.rqchallenge.common.exception;

import com.example.rqchallenge.common.enumer.ErrorInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BadRequestExceptionTest {

    private BadRequestException internalErrorExceptionUnderTest;

    @BeforeEach
    void setUp() {
        internalErrorExceptionUnderTest = new BadRequestException(ErrorInfo.UNEXPECTED_ERROR, "args");
    }

    @Test
    void testGetExternalMessage() {
        assertThat(internalErrorExceptionUnderTest.getMessage()).isEqualTo("An unexpected error has occurred: args");
    }

}