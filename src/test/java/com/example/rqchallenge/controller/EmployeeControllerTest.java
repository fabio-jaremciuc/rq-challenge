package com.example.rqchallenge.controller;

import com.example.rqchallenge.model.response.DetailedEmployeeResponse;
import com.example.rqchallenge.model.response.EmployeeResponse;
import com.example.rqchallenge.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("unit_test")
@DisplayName("Employee Controller Test")
class EmployeeControllerTest {

    private EmployeeService employeeService;

    private MockMvc mockMvc;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @BeforeEach
    void setUp() {
        employeeService = mock(EmployeeService.class);
        EmployeeController employeeController = new EmployeeController(employeeService);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    void getAllEmployees() throws Exception {

        when(employeeService.getAllEmployees()).thenReturn(List.of(new DetailedEmployeeResponse()));

        MvcResult result = mockMvc.perform(get("/")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        assertNotNull(result.getResponse().getContentAsString());
        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void getEmployeesByNameSearch() throws Exception {
        String nameSearch = "ll";

        String endpoint = String.format("/search/%s", nameSearch);

        when(employeeService.getEmployeesByNameSearch(nameSearch)).thenReturn(List.of(new DetailedEmployeeResponse()));

        MvcResult result = mockMvc.perform(get(endpoint)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        assertNotNull(result.getResponse().getContentAsString());
        verify(employeeService, times(1)).getEmployeesByNameSearch(anyString());
    }

    @Test
    void getEmployeeById() throws Exception {
        String employeeId = "1";

        String endpoint = String.format("/%s", employeeId);

        when(employeeService.getEmployeeById(employeeId)).thenReturn(new DetailedEmployeeResponse());

        MvcResult result = mockMvc.perform(get(endpoint)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(result.getResponse().getContentAsString());
        verify(employeeService, times(1)).getEmployeeById(anyString());
    }

    @Test
    void getHighestSalaryOfEmployees() throws Exception {
        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(100);

        MvcResult result = mockMvc.perform(get("/highestSalary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(result.getResponse().getContentAsString());
        verify(employeeService, times(1)).getHighestSalaryOfEmployees();
    }

    @Test
    void getTopTenHighestEarningEmployeeNames() throws Exception {
        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(List.of("John", "Paul"));

        MvcResult result = mockMvc.perform(get("/topTenHighestEarningEmployeeNames")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(result.getResponse().getContentAsString());
        verify(employeeService, times(1)).getTopTenHighestEarningEmployeeNames();
    }

    @Test
    void createEmployee() throws Exception {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "John Doe");
        employeeInput.put("salary", "50000.00");
        employeeInput.put("age", "30");

        when(employeeService.createEmployee(employeeInput)).thenReturn(new EmployeeResponse());

        MvcResult result = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsBytes(employeeInput)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn();

        assertNotNull(result.getResponse().getContentAsString());
        verify(employeeService, times(1)).createEmployee(employeeInput);
    }

    @Test
    void deleteEmployeeById() throws Exception {
        String employeeId = "1";
        String endpoint = String.format("/%s", employeeId);

        when(employeeService.deleteEmployeeById(employeeId)).thenReturn("John Doe");

        MvcResult result = mockMvc.perform(delete(endpoint)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(result.getResponse().getContentAsString());

        verify(employeeService, times(1)).deleteEmployeeById(eq(employeeId));
    }

}