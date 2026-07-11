package com.example.hospitalappointmentsystem;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalappointmentsystem.adapter.AdminDoctorAdapter;
import com.example.hospitalappointmentsystem.model.Doctor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ManageDoctorsActivity extends AppCompatActivity {

    private RecyclerView recyclerDoctors;
    private SearchView searchViewDoctor;
    private FloatingActionButton fabAddDoctor;

    private ArrayList<Doctor> doctorList;
    private ArrayList<Doctor> filteredList;

    private AdminDoctorAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_doctors);

        recyclerDoctors = findViewById(R.id.recyclerDoctors);
        searchViewDoctor = findViewById(R.id.searchViewDoctor);

        fabAddDoctor = findViewById(R.id.fabAddDoctor);

        recyclerDoctors.setLayoutManager(new LinearLayoutManager(this));

        doctorList = new ArrayList<>();
        filteredList = new ArrayList<>();

        adapter = new AdminDoctorAdapter(this, doctorList);
        recyclerDoctors.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadDoctors();

        fabAddDoctor.setOnClickListener(v ->
                startActivity(new Intent(
                        ManageDoctorsActivity.this,
                        AddDoctorActivity.class)));

        searchViewDoctor.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchDoctor(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchDoctor(newText);
                return true;
            }
        });
    }

    private void loadDoctors() {

        db.collection("doctors")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    doctorList.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                        Doctor doctor = document.toObject(Doctor.class);
                        doctor.setDoctorId(document.getId());

                        doctorList.add(doctor);
                    }

                    adapter.notifyDataSetChanged();

                });

    }

    private void searchDoctor(String text) {

        filteredList.clear();

        for (Doctor doctor : doctorList) {

            if (doctor.getName().toLowerCase().contains(text.toLowerCase())
                    || doctor.getSpecialization().toLowerCase().contains(text.toLowerCase())) {

                filteredList.add(doctor);
            }

        }

        adapter = new AdminDoctorAdapter(this, filteredList);
        recyclerDoctors.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDoctors();
    }
}