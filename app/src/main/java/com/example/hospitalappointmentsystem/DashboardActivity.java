package com.example.hospitalappointmentsystem;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    MaterialCardView cardDoctors, cardBook, cardMyAppointments,
            cardProfile, cardNotification, cardLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        cardDoctors = findViewById(R.id.cardDoctors);
        cardBook = findViewById(R.id.cardBook);
        cardMyAppointments = findViewById(R.id.cardMyAppointments);
        cardProfile = findViewById(R.id.cardProfile);
        cardNotification = findViewById(R.id.cardNotification);
        cardLogout = findViewById(R.id.cardLogout);

        cardDoctors.setOnClickListener(v ->
                startActivity(new Intent(this, DoctorActivity.class)));

        cardBook.setOnClickListener(v ->
                startActivity(new Intent(this, BookAppointmentActivity.class)));

        cardMyAppointments.setOnClickListener(v ->
                startActivity(new Intent(this, MyAppointmentsActivity.class)));

        cardProfile.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));

        cardNotification.setOnClickListener(v ->
                startActivity(new Intent(this, NotificationActivity.class)));

        cardLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}