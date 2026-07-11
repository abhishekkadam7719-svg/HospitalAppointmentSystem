package com.example.hospitalappointmentsystem;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalappointmentsystem.adapter.AdminAppointmentAdapter;
import com.example.hospitalappointmentsystem.model.Appointment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ManageAppointmentsActivity extends AppCompatActivity {

    RecyclerView recyclerAppointments;
    EditText etSearchAppointment;

    TextView btnAll, btnPending, btnApproved, btnRejected;

    AdminAppointmentAdapter adapter;

    ArrayList<Appointment> appointmentList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_appointments);

        recyclerAppointments = findViewById(R.id.recyclerAppointments);
        etSearchAppointment = findViewById(R.id.etSearchAppointment);

        btnAll = findViewById(R.id.btnAll);
        btnPending = findViewById(R.id.btnPending);
        btnApproved = findViewById(R.id.btnApproved);
        btnRejected = findViewById(R.id.btnRejected);

        recyclerAppointments.setLayoutManager(new LinearLayoutManager(this));

        appointmentList = new ArrayList<>();

        adapter = new AdminAppointmentAdapter(this, appointmentList);
        recyclerAppointments.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadAppointments();

        // Search
        etSearchAppointment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Filters
        btnAll.setOnClickListener(v -> filterStatus("All"));
        btnPending.setOnClickListener(v -> filterStatus("Pending"));
        btnApproved.setOnClickListener(v -> filterStatus("Approved"));
        btnRejected.setOnClickListener(v -> filterStatus("Rejected"));
    }

    private void loadAppointments() {

        db.collection("appointments")
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

    // Search
    private void filterSearch(String text) {

        ArrayList<Appointment> filteredList = new ArrayList<>();

        for (Appointment appointment : appointmentList) {

            String patient = appointment.getPatientName() == null ? "" : appointment.getPatientName().toLowerCase();

            String doctor = appointment.getDoctorName() == null ? "" : appointment.getDoctorName().toLowerCase();

            if (patient.contains(text.toLowerCase()) ||
                    doctor.contains(text.toLowerCase())) {

                filteredList.add(appointment);
            }
        }

        recyclerAppointments.setAdapter(
                new AdminAppointmentAdapter(this, filteredList));
    }

    // Status Filter
    private void filterStatus(String status) {

        if (status.equals("All")) {

            recyclerAppointments.setAdapter(
                    new AdminAppointmentAdapter(this, appointmentList));
            return;
        }

        ArrayList<Appointment> filteredList = new ArrayList<>();

        for (Appointment appointment : appointmentList) {

            if (appointment.getStatus() != null &&
                    appointment.getStatus().equalsIgnoreCase(status)) {

                filteredList.add(appointment);
            }
        }

        recyclerAppointments.setAdapter(
                new AdminAppointmentAdapter(this, filteredList));
    }
}