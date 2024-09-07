package com.example.rqchallenge.service.client.dto;

public class BaseEmployeeDTOResponse {
    private String status;
    private String message;

    public BaseEmployeeDTOResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
