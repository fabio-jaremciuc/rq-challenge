package com.example.rqchallenge.common.enumer;

public enum ErrorInfo {

    /* BAD REQUEST */
    BAD_REQUEST_STRING_HAS_ONLY_EMPTY_SPACES(1, "The entered string contains only whitespace"),
    BAD_REQUEST_STRING_ID_IT_IS_NOT_A_NUMBER(2, "Invalid ID format. ID must be a number (id=%s)"),

    /* NOT FOUND */
    NOT_FOUND_EMPLOYEE_BY_ID(1000, "No employee with the given id found (id=%s)"),
    NOT_FOUND_EMPLOYEES(1001, "No employees were found"),
    NOT_FOUND_HIGHEST_SALARY(1002, "Unable to find the highest salary"),
    NOT_FOUND_NO_NAME_MATCHES(1003, "No matching name found for the provided search string (searchString=%s)"),

    /* REMOTE API */
    REMOTE_API_HAS_RETURNED_AN_UNKNOWN_ERROR(2000,
        "The remote API has returned an unknown error - (errorMessage=%s)"),
    REMOTE_API_DOES_NOT_RETURN_BODY(2001, "The remote API did not return a response body"),

    UNEXPECTED_ERROR(3001, "An unexpected error has occurred: %s");

    private final Integer errorCode;
    private final String messageFormat;

    ErrorInfo(Integer errorCode, String message) {
        this.errorCode = errorCode;
        this.messageFormat = message;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getMessage(Object... args) {
        return String.format(messageFormat, args);
    }
}
