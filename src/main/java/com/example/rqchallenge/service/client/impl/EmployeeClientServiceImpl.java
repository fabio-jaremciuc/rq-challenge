package com.example.rqchallenge.service.client.impl;

import com.example.rqchallenge.common.enumer.ErrorInfo;
import com.example.rqchallenge.common.exception.InternalErrorException;
import com.example.rqchallenge.common.exception.NotFoundException;
import com.example.rqchallenge.service.client.Client;
import com.example.rqchallenge.service.client.EmployeeClientService;
import com.example.rqchallenge.service.client.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class EmployeeClientServiceImpl extends Client implements EmployeeClientService {

    @Value("${employee.service.base-url}")
    private String employeeBaseUrl;

    private static final String GET_EMPLOYEE_LIST_PATH = "/employees";

    private static final String GET_EMPLOYEE_PATH = "/employee/";

    private static final String CREATE_EMPLOYEE_PATH = "/create";

    private static final String DELETE_EMPLOYEE_PATH = "/delete/";

    private static final String LOG_RESPONSE = "[{}][{}] - Result: {}";

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeClientServiceImpl.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public EmployeeClientServiceImpl(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public String getServiceName() {
        return EmployeeClientService.class.getSimpleName();
    }

    @Override
    public List<EmployeeDataDTOResponse> getEmployees() {

        List<EmployeeDataDTOResponse> response = executeGetRequest(employeeBaseUrl + GET_EMPLOYEE_LIST_PATH,
                getServiceName(), EmployeesDTOResponse.class)
            .map(EmployeesDTOResponse::getEmployees)
            .filter(employees -> !CollectionUtils.isEmpty(employees))
            .orElseThrow(() -> new NotFoundException(ErrorInfo.NOT_FOUND_EMPLOYEES));

        LOG.info(LOG_RESPONSE, getServiceName(), "getEmployees", getResponseAsString(response));

        return response;
    }

    @Override
    public EmployeeDataDTOResponse getEmployeeById(String id) {
        EmployeeDataDTOResponse response = executeGetRequest(employeeBaseUrl + GET_EMPLOYEE_PATH + id, getServiceName(), EmployeeDTOResponse.class)
            .map(EmployeeDTOResponse::getEmployee)
            .orElseThrow(() -> new NotFoundException(ErrorInfo.NOT_FOUND_EMPLOYEE_BY_ID, id));

        LOG.info(LOG_RESPONSE, getServiceName(), "getEmployeeById", getResponseAsString(response));

        return response;
    }

    @Override
    public String deleteEmployeeById(String id) {
        String response = executeDeleteRequest(employeeBaseUrl + DELETE_EMPLOYEE_PATH + id, getServiceName(), BaseEmployeeDTOResponse.class)
            .map(BaseEmployeeDTOResponse::getMessage)
            .orElseThrow(() -> new NotFoundException(ErrorInfo.REMOTE_API_DOES_NOT_RETURN_BODY));

        LOG.info(LOG_RESPONSE, getServiceName(), "deleteEmployeeById", response);

        return response;
    }

    @Override
    public CreateEmployeeDataDTOResponse createEmployee(CreateEmployeeDTORequestBody employeeInput) {
        CreateEmployeeDataDTOResponse response = executePostRequest(employeeBaseUrl + CREATE_EMPLOYEE_PATH, getServiceName(), employeeInput, CreateEmployeeDTOResponse.class)
            .map(CreateEmployeeDTOResponse::getEmployee)
            .orElseThrow(() -> new InternalErrorException(ErrorInfo.REMOTE_API_DOES_NOT_RETURN_BODY));

        LOG.info(LOG_RESPONSE, getServiceName(), "createEmployee", getResponseAsString(response));

        return response;
    }

    private <T> String getResponseAsString(T response) {
        try {
            return OBJECT_MAPPER.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new InternalErrorException(ErrorInfo.UNEXPECTED_ERROR, e.getMessage());
        }
    }
}
