package com.example.rqchallenge.service.client.dto;

public class CreateEmployeeDTORequestBody {
    private String name;
    private String salary;
    private String age;

    public CreateEmployeeDTORequestBody() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
