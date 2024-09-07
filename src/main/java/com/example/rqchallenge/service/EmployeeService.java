package com.example.rqchallenge.service;

import com.example.rqchallenge.model.response.DetailedEmployeeResponse;
import com.example.rqchallenge.model.response.EmployeeResponse;

import java.util.List;
import java.util.Map;

public interface EmployeeService {
    List<DetailedEmployeeResponse> getAllEmployees();

    DetailedEmployeeResponse getEmployeeById(String id);

    EmployeeResponse createEmployee(Map<String, Object> employeeInput);

    String deleteEmployeeById(String id);

    List<DetailedEmployeeResponse> getEmployeesByNameSearch(String searchString);

    Integer getHighestSalaryOfEmployees();

    List<String> getTopTenHighestEarningEmployeeNames();
}
