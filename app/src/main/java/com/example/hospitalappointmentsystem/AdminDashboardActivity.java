package com.example.hospitalappointmentsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalappointmentsystem.adapter.AdminAppointmentAdapter;
import com.example.hospitalappointmentsystem.model.Appointment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AdminDashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerAdminAppointments;
    private AdminAppointmentAdapter adapter;
    private ArrayList<Appointment> appointmentList;

    private FirebaseFirestore db;

    private TextView tvDoctors, tvPatients, tvAppointments, tvPending;

    private Button btnAddDoctor;
    private Button btnManageAppointments;
    private Button btnNotifications;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        db = FirebaseFirestore.getInstance();

        // Dashboard Cards
        tvDoctors = findViewById(R.id.tvDoctors);
        tvPatients = findViewById(R.id.tvPatients);
        tvAppointments = findViewById(R.id.tvAppointments);
        tvPending = findViewById(R.id.tvPending);

        // Buttons
        btnAddDoctor = findViewById(R.id.btnAddDoctor);
        btnManageAppointments = findViewById(R.id.btnManageAppointments);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnLogout = findViewById(R.id.btnLogout);

        // RecyclerView
        recyclerAdminAppointments = findViewById(R.id.recyclerAdminAppointments);
        recyclerAdminAppointments.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdminAppointments.setNestedScrollingEnabled(false);

        appointmentList = new ArrayList<>();
        adapter = new AdminAppointmentAdapter(this, appointmentList);
        recyclerAdminAppointments.setAdapter(adapter);

        // Load data
        loadDashboardCounts();
        loadAppointments();

        // Add Doctor
        btnAddDoctor.setOnClickListener(v ->
                startActivity(new Intent(this, AddDoctorActivity.class)));

        // Manage Appointments
        btnManageAppointments.setOnClickListener(v ->
                startActivity(new Intent(this, ManageAppointmentsActivity.class)));

        // Notifications
        btnNotifications.setOnClickListener(v ->
                startActivity(new Intent(this, NotificationActivity.class)));

        // Logout
        btnLogout.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        });

    }

    private void loadDashboardCounts() {

        db.collection("doctors")
                .get()
                .addOnSuccessListener(snapshot ->
                        tvDoctors.setText(String.valueOf(snapshot.size())));

        db.collection("users")
                .get()
                .addOnSuccessListener(snapshot ->
                        tvPatients.setText(String.valueOf(snapshot.size())));

        db.collection("appointments")
                .get()
                .addOnSuccessListener(snapshot ->
                        tvAppointments.setText(String.valueOf(snapshot.size())));

        db.collection("appointments")
                .whereEqualTo("status", "Pending")
                .get()
                .addOnSuccessListener(snapshot ->
                        tvPending.setText(String.valueOf(snapshot.size())));
    }

    private void loadAppointments() {

        db.collection("appointments")
                .limit(5)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    appointmentList.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                        Appointment appointment = document.toObject(Appointment.class);
                        appointment.setId(document.getId());
                        appointmentList.add(appointment);

                    }

                    adapter.notifyDataSetChanged();

                });

    }

}