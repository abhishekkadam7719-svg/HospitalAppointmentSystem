package com.example.hospitalappointmentsystem.model;

public class Doctor {

    private String doctorId;
    private String name;
    private String specialization;
    private String experience;
    private String hospital;
    private String fees;
    private String rating;
    private String availability;
    private String imageUrl;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Doctor() {
        // Required empty constructor for Firestore
    }

    public Doctor(String doctorId, String name, String specialization,
                  String experience, String hospital, String fees,
                  String rating, String availability, String imageUrl) {

        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
        this.experience = experience;
        this.hospital = hospital;
        this.fees = fees;
        this.rating = rating;
        this.availability = availability;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}