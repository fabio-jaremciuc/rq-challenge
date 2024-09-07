package com.example.rqchallenge.controller;

import com.example.rqchallenge.model.response.DetailedEmployeeResponse;
import com.example.rqchallenge.model.response.EmployeeResponse;
import com.example.rqchallenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<DetailedEmployeeResponse>> getAllEmployees(){
        LOG.info("Get all employees");

        List<DetailedEmployeeResponse> employees = employeeService.getAllEmployees();

        return ResponseEntity.ok(employees);
    };

    @GetMapping(value = "/search/{searchString}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<DetailedEmployeeResponse>> getEmployeesByNameSearch(@PathVariable String searchString){

        LOG.info("Received request to search employees with name containing '{}'", searchString);

        List<DetailedEmployeeResponse> employees = employeeService.getEmployeesByNameSearch(searchString);

        return ResponseEntity.ok(employees);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DetailedEmployeeResponse> getEmployeeById(@PathVariable String id){

        LOG.info("Received request to get employee with ID '{}'", id);

        DetailedEmployeeResponse employee = employeeService.getEmployeeById(id);

        return ResponseEntity.ok(employee);
    }

    @GetMapping("/highestSalary")
    ResponseEntity<Integer> getHighestSalaryOfEmployees(){

        LOG.info("Received request to get highest salary of employees");

        Integer highestSalary = employeeService.getHighestSalaryOfEmployees();

        return ResponseEntity.ok(highestSalary);
    }

    @GetMapping(value = "/topTenHighestEarningEmployeeNames")
    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        LOG.info("Received request to get top ten highest earning employee names");

        List<String> topTenResult = employeeService.getTopTenHighestEarningEmployeeNames();

        return ResponseEntity.ok(topTenResult);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EmployeeResponse> createEmployee(@RequestBody Map<String, Object> employeeInput) {
        LOG.info("Received request to create employee with details: {}", employeeInput);

        EmployeeResponse employeeCreated = employeeService.createEmployee(employeeInput);

        return ResponseEntity.status(HttpStatus.CREATED).body(employeeCreated);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteEmployeeById(@PathVariable String id){

        LOG.info("Received request to delete employee with ID '{}'", id);

        String employeeDeletedMessage = employeeService.deleteEmployeeById(id);

        return ResponseEntity.ok(employeeDeletedMessage);
    }
}
