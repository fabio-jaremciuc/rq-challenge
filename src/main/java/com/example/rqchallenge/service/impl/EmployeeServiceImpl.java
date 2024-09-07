package com.example.rqchallenge.service.impl;

import com.example.rqchallenge.common.enumer.ErrorInfo;
import com.example.rqchallenge.common.exception.BadRequestException;
import com.example.rqchallenge.common.exception.NotFoundException;
import com.example.rqchallenge.model.response.DetailedEmployeeResponse;
import com.example.rqchallenge.model.response.EmployeeResponse;
import com.example.rqchallenge.service.EmployeeService;
import com.example.rqchallenge.service.client.EmployeeClientService;
import com.example.rqchallenge.service.client.dto.CreateEmployeeDTORequestBody;
import com.example.rqchallenge.service.client.dto.CreateEmployeeDataDTOResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeClientService employeeClientService;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static final int MAX_SIZE = 10;

    public EmployeeServiceImpl(EmployeeClientService employeeClientService) {
        this.employeeClientService = employeeClientService;
    }

    /*
     * output - list of employees
     * description - this should return all employees
     */
    @Override
    public List<DetailedEmployeeResponse> getAllEmployees() {
        return OBJECT_MAPPER.convertValue(employeeClientService.getEmployees(), new TypeReference<>(){});
    }

    /*
     * output - employee
     * description - this should return a single employee
     */
    @Override
    public DetailedEmployeeResponse getEmployeeById(String id) {
        validateId(id);
        return OBJECT_MAPPER.convertValue(employeeClientService.getEmployeeById(id), DetailedEmployeeResponse.class);
    }

    /*
     * output - string of the status (i.e. success)
     * description -  this should return a status of success or failed based on if an employee was created
     */
    @Override
    public EmployeeResponse createEmployee(Map<String, Object> employeeInput) {
        CreateEmployeeDTORequestBody requestBody = getCreateEmployeeRequestBody(employeeInput);

        CreateEmployeeDataDTOResponse response = employeeClientService.createEmployee(requestBody);
        return mapCreateEmployeeToResponse(response);
    }

    /*
     * output - the name of the employee that was deleted
     * description - this should delete the employee with specified id given
     */
    @Override
    public String deleteEmployeeById(String id) {
        validateId(id);

        DetailedEmployeeResponse employeeToBeDeleted = getEmployeeById(id);

        employeeClientService.deleteEmployeeById(id);

        return employeeToBeDeleted.getEmployeeName();
    }

    /*
     * output - list of employees
     * description - this should return all employees whose name contains or matches the string input provided
     */
    @Override
    public List<DetailedEmployeeResponse> getEmployeesByNameSearch(String searchString) {
        validateEmptyString(searchString);

        List<DetailedEmployeeResponse> employees = getAllEmployees();

         return Optional.of(employees.stream()
                .filter(employee -> employee.getEmployeeName() != null
                    && employee.getEmployeeName().toLowerCase().contains(searchString.toLowerCase()))
                .collect(Collectors.toList())
             )
            .filter(list -> !list.isEmpty())
            .orElseThrow(() -> new NotFoundException(ErrorInfo.NOT_FOUND_NO_NAME_MATCHES, searchString));
    }

    /*
    * output - integer of the highest salary
    * description -  this should return a single integer indicating the highest salary of all employees
    */
    @Override
    public Integer getHighestSalaryOfEmployees() {
        List<DetailedEmployeeResponse> employees = getAllEmployees();

        return employees.stream()
            .map(EmployeeResponse::getEmployeeSalary)
            .filter(Objects::nonNull)
            .max(Comparator.naturalOrder())
            .map(Double::intValue)
            .orElseThrow(() -> new NotFoundException(ErrorInfo.NOT_FOUND_HIGHEST_SALARY));
    }

    /*
     * output - list of employees
     * description -  this should return a list of the top 10 employees based off of their salaries
     */
    @Override
    public List<String> getTopTenHighestEarningEmployeeNames() {
        List<DetailedEmployeeResponse> employees = getAllEmployees();

        return Optional.of(employees.stream()
                .filter(employee -> employee.getEmployeeSalary() != null)
                .sorted(Comparator.comparingDouble(EmployeeResponse::getEmployeeSalary).reversed())
                .limit(MAX_SIZE)
                .map(EmployeeResponse::getEmployeeName)
                .collect(Collectors.toList()))
            .filter(list -> !list.isEmpty())
            .orElseThrow(() -> new NotFoundException(ErrorInfo.NOT_FOUND_HIGHEST_SALARY));
    }

    private static CreateEmployeeDTORequestBody getCreateEmployeeRequestBody(Map<String, Object> employeeInput) {
        CreateEmployeeDTORequestBody requestBody = new CreateEmployeeDTORequestBody();
        requestBody.setAge(employeeInput.get("age").toString());
        requestBody.setSalary(employeeInput.get("salary").toString());
        requestBody.setName(employeeInput.get("name").toString());
        return requestBody;
    }

    private EmployeeResponse mapCreateEmployeeToResponse(CreateEmployeeDataDTOResponse response) {
        EmployeeResponse employee = new EmployeeResponse();
        employee.setId(response.getId());
        employee.setEmployeeAge(response.getAge() != null ? Integer.valueOf(response.getAge()) : null);
        employee.setEmployeeSalary(response.getSalary() != null ? Double.valueOf(response.getSalary()) : null);
        employee.setEmployeeName(response.getName());
        return employee;
    }

    private void validateEmptyString(String string) {
        String stringWithEmptySpacesRemoved = string.trim();
        if (stringWithEmptySpacesRemoved.isEmpty()) {
            throw new BadRequestException(ErrorInfo.BAD_REQUEST_STRING_HAS_ONLY_EMPTY_SPACES);
        }
    }

    private void validateId(String id) {
        validateEmptyString(id);
        try {
            Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException(ErrorInfo.BAD_REQUEST_STRING_ID_IT_IS_NOT_A_NUMBER, id);
        }
    }
}
