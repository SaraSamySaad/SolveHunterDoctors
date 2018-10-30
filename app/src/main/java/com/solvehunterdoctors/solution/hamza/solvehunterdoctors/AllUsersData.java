package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

public class AllUsersData {
    private String gender;
    private String image;
    private String name;
    private String phone;

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public AllUsersData(String gender, String image, String name, String phone) {
        this.gender = gender;
        this.image = image;
        this.name = name;
        this.phone = phone;
    }

    public AllUsersData() {
    }
}
