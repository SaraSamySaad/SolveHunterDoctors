package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

public class AllDoctorsData {

    private String chatPrice;
    private String gender;
    private String image;
    private String name;
    private String phone;
    private String docId;

    public String getChatPrice() {
        return chatPrice;
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

    public String getType() {
        return type;
    }

    private String type;

    public AllDoctorsData(String chatPrice, String gender, String image, String name, String phone, String type) {
        this.chatPrice = chatPrice;
        this.gender = gender;
        this.image = image;
        this.name = name;
        this.phone = phone;
        this.type = type;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDocId() {
        return docId;
    }

    public AllDoctorsData() {
    }
}
