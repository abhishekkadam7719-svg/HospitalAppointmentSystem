package com.example.hospitalappointmentsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalappointmentsystem.adapter.AdminAppointmentAdapter;
import com.example.hospitalappointmentsystem.model.Appointment;
import com.google.android.material.card.MaterialCardView;
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
    private TextView tvViewAllAppointments;

    private MaterialCardView btnAddDoctor;
    private MaterialCardView btnManageAppointments;
    private MaterialCardView btnNotifications;
    private MaterialCardView btnManageDoctors;
    private CardView btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }

        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        db = FirebaseFirestore.getInstance();

        tvDoctors = findViewById(R.id.tvDoctors);
        tvPatients = findViewById(R.id.tvPatients);
        tvAppointments = findViewById(R.id.tvAppointments);
        tvPending = findViewById(R.id.tvPending);
        tvViewAllAppointments = findViewById(R.id.tvViewAllAppointments);

        btnAddDoctor = findViewById(R.id.btnAddDoctor);
        btnManageAppointments = findViewById(R.id.btnManageAppointments);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnManageDoctors = findViewById(R.id.btnManageDoctors);

        recyclerAdminAppointments = findViewById(R.id.recyclerAdminAppointments);
        recyclerAdminAppointments.setLayoutManager(new LinearLayoutManager(this));

        appointmentList = new ArrayList<>();
        adapter = new AdminAppointmentAdapter(this, appointmentList);
        recyclerAdminAppointments.setAdapter(adapter);

        loadDashboardCounts();
        loadAppointments();

        btnAddDoctor.setOnClickListener(v ->
                startActivity(new Intent(this, AddDoctorActivity.class)));

        btnManageAppointments.setOnClickListener(v ->
                startActivity(new Intent(this, ManageAppointmentsActivity.class)));

        btnNotifications.setOnClickListener(v ->
                startActivity(new Intent(this, NotificationActivity.class)));

        btnManageDoctors.setOnClickListener(v ->
                startActivity(new Intent(this, ManageDoctorsActivity.class)));

        tvViewAllAppointments.setOnClickListener(v ->
                startActivity(new Intent(this, ManageAppointmentsActivity.class)));
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

                        Appointment appointment =
                                document.toObject(Appointment.class);

                        appointment.setId(document.getId());

                        appointmentList.add(appointment);
                    }

                    adapter.notifyDataSetChanged();
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_logout) {

            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}