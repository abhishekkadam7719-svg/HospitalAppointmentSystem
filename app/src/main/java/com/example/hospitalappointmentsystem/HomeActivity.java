package com.example.hospitalappointmentsystem;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalappointmentsystem.adapter.DoctorAdapter;
import com.example.hospitalappointmentsystem.model.Doctor;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private MaterialCardView cardHeart, cardDental, cardEye, cardBone;
    private RecyclerView recyclerTopDoctors;
    private DoctorAdapter adapter;
    private ArrayList<Doctor> doctorList;
    private FirebaseFirestore db;
    private BottomNavigationView bottomNav;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_home);

        // Initialize Views
        cardHeart = findViewById(R.id.cardHeart);
        cardDental = findViewById(R.id.cardDental);
        cardEye = findViewById(R.id.cardEye);
        cardBone = findViewById(R.id.cardBone);

        searchView = findViewById(R.id.searchView);

        recyclerTopDoctors = findViewById(R.id.recyclerTopDoctors);
        bottomNav = findViewById(R.id.bottomNav);

        recyclerTopDoctors.setLayoutManager(new LinearLayoutManager(this));

        doctorList = new ArrayList<>();
        adapter = new DoctorAdapter(doctorList);
        recyclerTopDoctors.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadDoctors();

        // Search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchDoctors(newText);
                return true;
            }
        });

        // Category Clicks
        cardHeart.setOnClickListener(v ->
                filterDoctorsByCategory("Cardiologist"));

        cardDental.setOnClickListener(v ->
                filterDoctorsByCategory("Dentist"));

        cardEye.setOnClickListener(v ->
                filterDoctorsByCategory("Ophthalmologist"));

        cardBone.setOnClickListener(v ->
                filterDoctorsByCategory("Orthopedic"));

        // Bottom Navigation
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;

            } else if (id == R.id.nav_doctors) {
                startActivity(new Intent(HomeActivity.this, DoctorActivity.class));
                return true;

            } else if (id == R.id.nav_appointments) {
                startActivity(new Intent(HomeActivity.this, MyAppointmentsActivity.class));
                return true;

            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                return true;
            }

            return false;
        });
    }

    // Load Doctors
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

    // Search Doctors
    private void searchDoctors(String text) {

        ArrayList<Doctor> filteredList = new ArrayList<>();

        for (Doctor doctor : doctorList) {

            if (doctor.getName().toLowerCase().contains(text.toLowerCase())
                    || doctor.getSpecialization().toLowerCase().contains(text.toLowerCase())) {

                filteredList.add(doctor);
            }
        }

        adapter.filterList(filteredList);
    }

    // Filter Category
    private void filterDoctorsByCategory(String specialization) {

        ArrayList<Doctor> filteredList = new ArrayList<>();

        for (Doctor doctor : doctorList) {

            if (doctor.getSpecialization().equalsIgnoreCase(specialization)) {

                filteredList.add(doctor);
            }
        }

        adapter.filterList(filteredList);
    }
}