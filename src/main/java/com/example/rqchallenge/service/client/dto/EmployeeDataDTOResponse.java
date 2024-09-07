package com.example.rqchallenge.service.client.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class EmployeeDataDTOResponse {
    private Integer id;
    @JsonAlias("employee_name")
    private String employeeName;
    @JsonAlias("employee_salary")
    private Double employeeSalary;
    @JsonAlias("employee_age")
    private Integer employeeAge;
    @JsonAlias("profile_image")
    private String profileImage;

    public EmployeeDataDTOResponse() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Double getEmployeeSalary() {
        return employeeSalary;
    }

    public void setEmployeeSalary(Double employeeSalary) {
        this.employeeSalary = employeeSalary;
    }

    public Integer getEmployeeAge() {
        return employeeAge;
    }

    public void setEmployeeAge(Integer employeeAge) {
        this.employeeAge = employeeAge;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
