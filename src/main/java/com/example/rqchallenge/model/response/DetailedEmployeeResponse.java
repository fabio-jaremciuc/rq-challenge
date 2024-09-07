package com.example.rqchallenge.model.response;

public class DetailedEmployeeResponse extends EmployeeResponse {
    private String profileImage;

    public DetailedEmployeeResponse() {
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
