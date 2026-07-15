package com.example.hospitalappointmentsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalappointmentsystem.adapter.DoctorAdapter;
import com.example.hospitalappointmentsystem.model.Doctor;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
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
    private TextView txtViewAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        cardHeart = findViewById(R.id.cardHeart);
        cardDental = findViewById(R.id.cardDental);
        cardEye = findViewById(R.id.cardEye);
        cardBone = findViewById(R.id.cardBone);

        searchView = findViewById(R.id.searchView);
        recyclerTopDoctors = findViewById(R.id.recyclerTopDoctors);
        bottomNav = findViewById(R.id.bottomNav);
        searchView = findViewById(R.id.searchView);
        recyclerTopDoctors = findViewById(R.id.recyclerTopDoctors);
        bottomNav = findViewById(R.id.bottomNav);

        txtViewAll = findViewById(R.id.txtViewAll);

        txtViewAll.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, DoctorActivity.class);
            startActivity(intent);
        });

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
                searchDoctors(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchDoctors(newText);
                return true;
            }
        });

        // Categories
        cardHeart.setOnClickListener(v -> filterDoctorsByCategory("Cardiologist"));

        cardDental.setOnClickListener(v -> filterDoctorsByCategory("Dentist"));

        cardEye.setOnClickListener(v -> filterDoctorsByCategory("Ophthalmologist"));

        cardBone.setOnClickListener(v -> filterDoctorsByCategory("Orthopedic"));

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

    private void loadDoctors() {

        db.collection("doctors")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    doctorList.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                        Doctor doctor = document.toObject(Doctor.class);
                        doctorList.add(doctor);
                    }

                    adapter.filterList(new ArrayList<>(doctorList));

                });
    }
    // Search Doctors
    private void searchDoctors(String text) {

        if (text == null || text.trim().isEmpty()) {
            adapter.filterList(new ArrayList<>(doctorList));
            return;
        }

        ArrayList<Doctor> filteredList = new ArrayList<>();

        for (Doctor doctor : doctorList) {

            String name = doctor.getName() == null ? "" : doctor.getName();
            String specialization = doctor.getSpecialization() == null ? "" : doctor.getSpecialization();

            if (name.toLowerCase().contains(text.toLowerCase())
                    || specialization.toLowerCase().contains(text.toLowerCase())) {

                filteredList.add(doctor);
            }
        }

        adapter.filterList(filteredList);
    }

    // Filter Doctors by Category
    private void filterDoctorsByCategory(String specialization) {

        ArrayList<Doctor> filteredList = new ArrayList<>();

        for (Doctor doctor : doctorList) {

            if (doctor.getSpecialization() != null &&
                    doctor.getSpecialization().equalsIgnoreCase(specialization)) {

                filteredList.add(doctor);
            }
        }

        adapter.filterList(filteredList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_notification) {

            startActivity(new Intent(this, NotificationActivity.class));
            return true;

        } else if (id == R.id.menu_logout) {

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