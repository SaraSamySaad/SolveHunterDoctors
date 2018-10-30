package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

/**
 * Created by C.M on 25/10/2018.
 */

public class AddDoctorData {
    private String name;
    private String chatPrice;
    private String phone;
    private String type;
    private  String image;
    private String gender;
    private String medicalSpecialty;

    public void setMedicalSpecialty(String medicalSpecialty) {
        this.medicalSpecialty = medicalSpecialty;
    }

    public String getMedicalSpecialty() {
        return medicalSpecialty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChatPrice(String chatPrice) {
        this.chatPrice = chatPrice;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getChatPrice() {
        return chatPrice;
    }

    public String getPhone() {
        return phone;
    }

    public String getType() {
        return type;
    }

    public String getImage() {
        return image;
    }

    public AddDoctorData() {
    }

    public String getGender() {
        return gender;
    }

    public AddDoctorData(String name, String chatPrice, String phone, String type, String image, String gender, String medicalSpecialty) {
        this.name = name;
        this.chatPrice = chatPrice;
        this.phone = phone;
        this.type = type;
        this.image = image;
        this.gender = gender;
        this.medicalSpecialty = medicalSpecialty;
    }
}
