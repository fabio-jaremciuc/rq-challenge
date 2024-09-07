package com.example.rqchallenge.service.client.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public class EmployeesDTOResponse extends BaseEmployeeDTOResponse {
    @JsonAlias("data")
    private List<EmployeeDataDTOResponse> employees;

    public EmployeesDTOResponse() {
    }

    public List<EmployeeDataDTOResponse> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeDataDTOResponse> employees) {
        this.employees = employees;
    }
}
