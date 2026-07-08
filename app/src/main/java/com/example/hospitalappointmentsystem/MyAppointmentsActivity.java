package com.example.hospitalappointmentsystem;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalappointmentsystem.adapter.AppointmentAdapter;
import com.example.hospitalappointmentsystem.model.Appointment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MyAppointmentsActivity extends AppCompatActivity {

    RecyclerView recyclerAppointments;
    AppointmentAdapter adapter;
    ArrayList<Appointment> appointmentList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointments);

        recyclerAppointments = findViewById(R.id.recyclerAppointments);

        recyclerAppointments.setLayoutManager(new LinearLayoutManager(this));

        appointmentList = new ArrayList<>();
        adapter = new AppointmentAdapter(appointmentList);
        recyclerAppointments.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadAppointments();
    }

    private void loadAppointments() {

        db.collection("appointments")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    appointmentList.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                        Appointment appointment = document.toObject(Appointment.class);
                        appointmentList.add(appointment);
                    }

                    adapter.notifyDataSetChanged();
                });
    }
}