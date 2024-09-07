package com.example.rqchallenge.service.client.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class CreateEmployeeDTOResponse extends BaseEmployeeDTOResponse {
    @JsonAlias("data")
    private CreateEmployeeDataDTOResponse employee;

    public CreateEmployeeDTOResponse() {
    }

    public CreateEmployeeDataDTOResponse getEmployee() {
        return employee;
    }

    public void setEmployee(CreateEmployeeDataDTOResponse employee) {
        this.employee = employee;
    }
}
