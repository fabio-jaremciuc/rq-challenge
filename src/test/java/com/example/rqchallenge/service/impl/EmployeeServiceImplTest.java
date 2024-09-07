package com.example.rqchallenge.service.impl;

import com.example.rqchallenge.common.enumer.ErrorInfo;
import com.example.rqchallenge.common.exception.BadRequestException;
import com.example.rqchallenge.common.exception.NotFoundException;
import com.example.rqchallenge.model.response.DetailedEmployeeResponse;
import com.example.rqchallenge.model.response.EmployeeResponse;
import com.example.rqchallenge.service.EmployeeService;
import com.example.rqchallenge.service.client.EmployeeClientService;
import com.example.rqchallenge.service.client.dto.CreateEmployeeDataDTOResponse;
import com.example.rqchallenge.service.client.dto.EmployeeDataDTOResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Employee Service Test")
class EmployeeServiceImplTest {

    private EmployeeService employeeService;

    private EmployeeClientService employeeClient;

    @BeforeEach
    void setUp() {
        employeeClient = mock(EmployeeClientService.class);
        employeeService = new EmployeeServiceImpl(employeeClient);
    }

    @Test
    void getAllEmployees() {
        EmployeeDataDTOResponse employeeOne = new EmployeeDataDTOResponse();
        EmployeeDataDTOResponse employeeTwo = new EmployeeDataDTOResponse();

        when(employeeClient.getEmployees()).thenReturn(List.of(employeeOne, employeeTwo));

        List<DetailedEmployeeResponse> employees = employeeService.getAllEmployees();

        assertEquals(2, employees.size());

        verify(employeeClient, times(1)).getEmployees();
    }

    @Test
    void deleteEmployeeById() {
        String employeeId = "1";
        String johnDoe = "John Doe";

        EmployeeDataDTOResponse employeeToBeDeleted = new EmployeeDataDTOResponse();
        employeeToBeDeleted.setEmployeeName(johnDoe);
        when(employeeClient.getEmployeeById(employeeId)).thenReturn(employeeToBeDeleted);
        when(employeeClient.deleteEmployeeById(employeeId)).thenReturn("Deleted");

        String message = employeeService.deleteEmployeeById(employeeId);

        assertEquals(johnDoe, message);

        verify(employeeClient, times(1)).deleteEmployeeById(eq(employeeId));
        verify(employeeClient, times(1)).deleteEmployeeById(eq(employeeId));
    }

    @Test
    void deleteEmployeeById_badRequestIdNotANumber() {
        String employeeId = "fsdfdsfsdfsd";

        BadRequestException expected =
                new BadRequestException(ErrorInfo.BAD_REQUEST_STRING_ID_IT_IS_NOT_A_NUMBER, employeeId);
        BadRequestException actual =
                assertThrows(BadRequestException.class, () -> employeeService.deleteEmployeeById(employeeId));

        assertEquals(expected.getMessage(), actual.getMessage());

        verify(employeeClient, times(0)).deleteEmployeeById(eq(employeeId));
    }

    @Test
    void createEmployee() {
        CreateEmployeeDataDTOResponse response = new CreateEmployeeDataDTOResponse();
        response.setAge("10");
        response.setName("name");
        response.setSalary("10000");
        response.setId(1);
        when(employeeClient.createEmployee(any())).thenReturn(response);

        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "John Doe");
        employeeInput.put("salary", "50000.00");
        employeeInput.put("age", "30");
        EmployeeResponse message = employeeService.createEmployee(employeeInput);

        assertEquals(response.getAge(), String.valueOf(message.getEmployeeAge()));
        verify(employeeClient, times(1)).createEmployee(any());
    }

    @Test
    void getAllEmployeeById() {
        String employeeId = "1";
        EmployeeDataDTOResponse employeeResponse = new EmployeeDataDTOResponse();
        employeeResponse.setId(Integer.valueOf(employeeId));

        when(employeeClient.getEmployeeById(employeeId)).thenReturn(employeeResponse);

        EmployeeResponse employees = employeeService.getEmployeeById(employeeId);

        assertEquals(Integer.valueOf(employeeId), employees.getId());
        verify(employeeClient, times(1)).getEmployeeById(eq(employeeId));
    }

    @Test
    void getAllEmployeeById_badRequestIdNotANumber() {
        String employeeId = "fsdfdsfsdfsd";

        BadRequestException expected =
                new BadRequestException(ErrorInfo.BAD_REQUEST_STRING_ID_IT_IS_NOT_A_NUMBER, employeeId);
        BadRequestException actual =
                assertThrows(BadRequestException.class, () -> employeeService.getEmployeeById(employeeId));

        assertEquals(expected.getMessage(), actual.getMessage());

        verify(employeeClient, times(0)).getEmployeeById(eq(employeeId));
    }

    @Test
    void getEmployeesByNameSearch() {
        EmployeeDataDTOResponse employeeJohn = new EmployeeDataDTOResponse();
        employeeJohn.setEmployeeName("John");
        EmployeeDataDTOResponse employeeJane = new EmployeeDataDTOResponse();
        employeeJane.setEmployeeName("Jane");

        when(employeeClient.getEmployees()).thenReturn(List.of(employeeJohn, employeeJane));

        List<DetailedEmployeeResponse> employeesByName = employeeService.getEmployeesByNameSearch("John");
        assertEquals(employeesByName.get(0).getEmployeeName(), employeeJohn.getEmployeeName());

        List<DetailedEmployeeResponse> employeesByLetter = employeeService.getEmployeesByNameSearch("J");
        assertEquals(2, employeesByLetter.size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"x", " err "})
    void getEmployeesByNameSearch_notFoundParameterDoesNotMatches(String searchString) {
        EmployeeDataDTOResponse employeeJohn = new EmployeeDataDTOResponse();
        employeeJohn.setEmployeeName("John");
        EmployeeDataDTOResponse employeeJane = new EmployeeDataDTOResponse();
        employeeJane.setEmployeeName("Jane");

        when(employeeClient.getEmployees()).thenReturn(List.of(employeeJohn, employeeJane));

        NotFoundException expected =
                new NotFoundException(ErrorInfo.NOT_FOUND_NO_NAME_MATCHES, searchString);
        NotFoundException actual =
            assertThrows(NotFoundException.class, () -> employeeService.getEmployeesByNameSearch(searchString));

        assertEquals(expected.getMessage(), actual.getMessage());

        verify(employeeClient, times(1)).getEmployees();
    }

    @Test
    void getEmployeesByNameSearch_badRequestEmptyString() {
        String searchStringEmpty = "          ";

        BadRequestException expected =
                new BadRequestException(ErrorInfo.BAD_REQUEST_STRING_HAS_ONLY_EMPTY_SPACES);

        BadRequestException actual =
                assertThrows(BadRequestException.class, () -> employeeService.getEmployeesByNameSearch(searchStringEmpty));

        assertEquals(expected.getMessage(), actual.getMessage());

        verify(employeeClient, times(0)).getEmployees();
    }

    @Test
    void getHighestSalaryOfEmployees() {
        EmployeeDataDTOResponse employeeOne = new EmployeeDataDTOResponse();
        employeeOne.setEmployeeSalary(1.0);
        EmployeeDataDTOResponse employeeTwo = new EmployeeDataDTOResponse();
        employeeTwo.setEmployeeSalary(2.0);
        EmployeeDataDTOResponse employeeThree = new EmployeeDataDTOResponse();
        double highestSalary = 3.0;
        employeeThree.setEmployeeSalary(highestSalary);

        when(employeeClient.getEmployees()).thenReturn(List.of(employeeOne, employeeTwo, employeeThree));

        Integer highestSalaryResponse = employeeService.getHighestSalaryOfEmployees();

        assertEquals((int) highestSalary, highestSalaryResponse);

        verify(employeeClient, times(1)).getEmployees();
    }

    @Test
    void getHighestSalaryOfEmployees_notFoundException() {
        EmployeeDataDTOResponse employeeOne = new EmployeeDataDTOResponse();
        EmployeeDataDTOResponse employeeTwo = new EmployeeDataDTOResponse();
        EmployeeDataDTOResponse employeeThree = new EmployeeDataDTOResponse();

        when(employeeClient.getEmployees()).thenReturn(List.of(employeeOne, employeeTwo, employeeThree));

        NotFoundException expected =
                new NotFoundException(ErrorInfo.NOT_FOUND_HIGHEST_SALARY);
        NotFoundException actual =
                assertThrows(NotFoundException.class, () -> employeeService.getHighestSalaryOfEmployees());

        assertEquals(expected.getMessage(), actual.getMessage());

        verify(employeeClient, times(1)).getEmployees();
    }

    @Test
    void getTopTenHighestEarningEmployeeNames() {

        List<EmployeeDataDTOResponse> employeeDataResponses = getEmployeesWithSalaries();
        when(employeeClient.getEmployees()).thenReturn(employeeDataResponses);

        List<String> topTenHighestEarningEmployeeNames = employeeService.getTopTenHighestEarningEmployeeNames();

        assertEquals(10, topTenHighestEarningEmployeeNames.size());

        EmployeeDataDTOResponse employeeWithLowestSalary = Collections.min(employeeDataResponses,
            Comparator.comparingDouble(EmployeeDataDTOResponse::getEmployeeSalary));

        assertFalse(topTenHighestEarningEmployeeNames.contains(employeeWithLowestSalary.getEmployeeName()));

        verify(employeeClient, times(1)).getEmployees();
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_notFoundException() {
        EmployeeDataDTOResponse employeeOne = new EmployeeDataDTOResponse();
        EmployeeDataDTOResponse employeeTwo = new EmployeeDataDTOResponse();
        EmployeeDataDTOResponse employeeThree = new EmployeeDataDTOResponse();

        when(employeeClient.getEmployees()).thenReturn(List.of(employeeOne, employeeTwo, employeeThree));

        NotFoundException expected =
                new NotFoundException(ErrorInfo.NOT_FOUND_HIGHEST_SALARY);
        NotFoundException actual =
                assertThrows(NotFoundException.class, () -> employeeService.getTopTenHighestEarningEmployeeNames());

        assertEquals(expected.getMessage(), actual.getMessage());

        verify(employeeClient, times(1)).getEmployees();
    }

    private List<EmployeeDataDTOResponse> getEmployeesWithSalaries() {
        List<EmployeeDataDTOResponse> employees = new ArrayList<>();
        EmployeeDataDTOResponse employeeLowestSalary = new EmployeeDataDTOResponse();
        employeeLowestSalary.setEmployeeName("name_1");
        employeeLowestSalary.setEmployeeSalary(1.0);
        employees.add(employeeLowestSalary);
        EmployeeDataDTOResponse employeeTwo = new EmployeeDataDTOResponse();
        employeeTwo.setEmployeeName("name_2");
        employeeTwo.setEmployeeSalary(2.0);
        employees.add(employeeTwo);
        EmployeeDataDTOResponse employeeThree = new EmployeeDataDTOResponse();
        employeeThree.setEmployeeName("name_3");
        employeeThree.setEmployeeSalary(3.0);
        employees.add(employeeThree);
        EmployeeDataDTOResponse employeeFour = new EmployeeDataDTOResponse();
        employeeFour.setEmployeeName("name_4");
        employeeFour.setEmployeeSalary(4.0);
        employees.add(employeeFour);
        EmployeeDataDTOResponse employeeFive = new EmployeeDataDTOResponse();
        employeeFive.setEmployeeName("name_5");
        employeeFive.setEmployeeSalary(5.0);
        employees.add(employeeFive);
        EmployeeDataDTOResponse employeeSix = new EmployeeDataDTOResponse();
        employeeSix.setEmployeeName("name_6");
        employeeSix.setEmployeeSalary(6.0);
        employees.add(employeeSix);
        EmployeeDataDTOResponse employeeSeven = new EmployeeDataDTOResponse();
        employeeSeven.setEmployeeName("name_7");
        employeeSeven.setEmployeeSalary(7.0);
        employees.add(employeeSeven);
        EmployeeDataDTOResponse employeeEight = new EmployeeDataDTOResponse();
        employeeEight.setEmployeeName("name_8");
        employeeEight.setEmployeeSalary(8.0);
        employees.add(employeeEight);
        EmployeeDataDTOResponse employeeNine = new EmployeeDataDTOResponse();
        employeeNine.setEmployeeName("name_9");
        employeeNine.setEmployeeSalary(9.0);
        employees.add(employeeNine);
        EmployeeDataDTOResponse employeeTen = new EmployeeDataDTOResponse();
        employeeTen.setEmployeeName("name_10");
        employeeTen.setEmployeeSalary(10.0);
        employees.add(employeeTen);
        EmployeeDataDTOResponse employeeEleven = new EmployeeDataDTOResponse();
        employeeEleven.setEmployeeName("name_11");
        employeeEleven.setEmployeeSalary(11.0);
        employees.add(employeeEleven);
        return employees;
    }

}