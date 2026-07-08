package com.example.hospitalappointmentsystem;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalappointmentsystem.adapter.DoctorAdapter;
import com.example.hospitalappointmentsystem.model.Doctor;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class DoctorActivity extends AppCompatActivity {

    RecyclerView recyclerDoctors;
    ArrayList<Doctor> doctorList;
    DoctorAdapter adapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        recyclerDoctors = findViewById(R.id.recyclerDoctors);
        recyclerDoctors.setLayoutManager(new LinearLayoutManager(this));

        doctorList = new ArrayList<>();
        adapter = new DoctorAdapter(doctorList);
        recyclerDoctors.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadDoctors();

    }

    private void loadDoctors() {

        db.collection("doctors")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    doctorList.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                        Doctor doctor = document.toObject(Doctor.class);
                        doctorList.add(doctor);

                    }

                    adapter.notifyDataSetChanged();

                });
    }
}