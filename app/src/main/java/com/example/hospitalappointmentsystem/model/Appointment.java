package com.example.hospitalappointmentsystem.model;

public class Appointment {

    private String doctorName;
    private String specialization;
    private String fees;
    private String patientName;
    private String phone;
    private String symptoms;
    private String date;
    private String time;
    private String status;

    // Required empty constructor for Firestore
    public Appointment() {
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getFees() {
        return fees;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPhone() {
        return phone;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
}