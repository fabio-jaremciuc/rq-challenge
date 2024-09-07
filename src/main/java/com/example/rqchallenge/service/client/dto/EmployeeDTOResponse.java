package com.example.rqchallenge.service.client.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class EmployeeDTOResponse extends BaseEmployeeDTOResponse {
    @JsonAlias("data")
    private EmployeeDataDTOResponse employee;

    public EmployeeDTOResponse() {
    }

    public EmployeeDataDTOResponse getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDataDTOResponse employee) {
        this.employee = employee;
    }
}
