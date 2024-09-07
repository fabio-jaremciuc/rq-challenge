package com.example.rqchallenge.service.client;

import com.example.rqchallenge.service.client.dto.CreateEmployeeDTORequestBody;
import com.example.rqchallenge.service.client.dto.CreateEmployeeDataDTOResponse;
import com.example.rqchallenge.service.client.dto.EmployeeDataDTOResponse;

import java.util.List;

public interface EmployeeClientService {
    List<EmployeeDataDTOResponse> getEmployees();

    EmployeeDataDTOResponse getEmployeeById(String id);

    String deleteEmployeeById(String id);

    CreateEmployeeDataDTOResponse createEmployee(CreateEmployeeDTORequestBody employeeInput);
}
