package com.example.rqchallenge.service.client.impl;

import com.example.rqchallenge.service.client.EmployeeClientService;
import com.example.rqchallenge.service.client.dto.CreateEmployeeDTORequestBody;
import com.example.rqchallenge.service.client.dto.CreateEmployeeDataDTOResponse;
import com.example.rqchallenge.service.client.dto.EmployeeDataDTOResponse;
import com.example.rqchallenge.spring.SpringConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("unit_test")
@ContextConfiguration(classes = {
    SpringConfiguration.class,
    EmployeeClientServiceImpl.class
})
class EmployeeClientServiceImplTest {

    @Autowired
    private EmployeeClientService employeeClientService;

    private static final WireMockServer WIRE_MOCK_SERVER =
        new WireMockServer(WireMockConfiguration.wireMockConfig().port(8081));

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static String getEmployeesResponseAsString;
    private static String getEmployeeByIdResponseAsString;
    private static String deleteEmployeeByIdResponseAsString;
    private static String createEmployeeRequestAsString;
    private static String createEmployeeResponseAsString;

    private static String readFileAsString(String uriFile) throws IOException {
        return new String(Files.readAllBytes(Paths.get(uriFile)));
    }

    @BeforeAll
    public static void init() throws IOException {
        WIRE_MOCK_SERVER.start();
        getEmployeesResponseAsString = readFileAsString("src/test/resources/json/employeesservice/getEmployees.json");
        getEmployeeByIdResponseAsString = readFileAsString("src/test/resources/json/employeesservice/getEmployeeById.json");
        deleteEmployeeByIdResponseAsString = readFileAsString("src/test/resources/json/employeesservice/deleteEmployeeById.json");
        createEmployeeRequestAsString = readFileAsString("src/test/resources/json/employeesservice/createEmployeeRequest.json");
        createEmployeeResponseAsString = readFileAsString("src/test/resources/json/employeesservice/createEmployeeResponse.json");
    }

    @AfterAll
    public static void cleanUp() {
        WIRE_MOCK_SERVER.shutdown();
    }

    @Test
    void getEmployees() {

        WIRE_MOCK_SERVER.stubFor(WireMock.get(urlPathTemplate("/employees"))
            .willReturn(jsonResponse(getEmployeesResponseAsString, HttpStatus.OK.value())));

        List<EmployeeDataDTOResponse> dataResponses = employeeClientService.getEmployees();

        assertEquals(24, dataResponses.size());
    }

    @Test
    void getEmployeeById() {

        WIRE_MOCK_SERVER.stubFor(WireMock.get(urlPathTemplate("/employee/1"))
            .willReturn(jsonResponse(getEmployeeByIdResponseAsString, HttpStatus.OK.value())));

        EmployeeDataDTOResponse dataResponses = employeeClientService.getEmployeeById("1");

        assertEquals(1, dataResponses.getId());
    }

    @Test
    void deleteEmployeeById() {
        WIRE_MOCK_SERVER.stubFor(WireMock.delete(urlPathTemplate("/delete/1"))
            .willReturn(jsonResponse(deleteEmployeeByIdResponseAsString, HttpStatus.OK.value())));

        String dataResponses = employeeClientService.deleteEmployeeById("1");

        assertEquals("Successfully! All records has been deleted.", dataResponses);
    }

    @Test
    void createEmployee() throws JsonProcessingException {
        WIRE_MOCK_SERVER.stubFor(WireMock.post(urlPathTemplate("/create"))
            .withRequestBody(equalToJson(createEmployeeRequestAsString))
            .willReturn(jsonResponse(createEmployeeResponseAsString, HttpStatus.CREATED.value())));


        CreateEmployeeDTORequestBody requestBody = OBJECT_MAPPER.readValue(createEmployeeRequestAsString, CreateEmployeeDTORequestBody.class);
        CreateEmployeeDataDTOResponse dataResponses = employeeClientService.createEmployee(requestBody);

        assertEquals(1, dataResponses.getId());
    }
}