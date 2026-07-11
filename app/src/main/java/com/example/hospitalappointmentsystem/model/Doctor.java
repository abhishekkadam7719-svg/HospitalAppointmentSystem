package com.example.hospitalappointmentsystem.model;

public class Doctor {

    private String doctorId;
    private String name;
    private String specialization;
    private String experience;
    private String hospital;
    private String fees;
    private String phone;
    private String rating;
    private String availability;
    private String image;

    public Doctor() {
        // Required for Firestore
    }

    // Constructor used by AddDoctorActivity
    public Doctor(String name,
                  String specialization,
                  String hospital,
                  String experience,
                  String fees,
                  String phone,
                  String image) {

        this.name = name;
        this.specialization = specialization;
        this.hospital = hospital;
        this.experience = experience;
        this.fees = fees;
        this.phone = phone;
        this.image = image;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getImage() {
        return image;
    }

    public void setImageUrl(String image) {
        this.image = image;
    }
}