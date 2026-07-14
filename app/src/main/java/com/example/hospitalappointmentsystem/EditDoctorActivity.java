package com.example.hospitalappointmentsystem;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditDoctorActivity extends AppCompatActivity {

    private TextInputEditText etDoctorName, etSpecialization,
            etHospital, etExperience, etFees, etRating;

    private Spinner spinnerAvailability;
    private Button btnUpdateDoctor;
    private TextView btnDeleteDoctor;

    private FirebaseFirestore db;

    private String doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor);

        etDoctorName = findViewById(R.id.etDoctorName);
        etSpecialization = findViewById(R.id.etSpecialization);
        etHospital = findViewById(R.id.etHospital);
        etExperience = findViewById(R.id.etExperience);
        etFees = findViewById(R.id.etFees);
        etRating = findViewById(R.id.etRating);

        spinnerAvailability = findViewById(R.id.spinnerAvailability);

        btnUpdateDoctor = findViewById(R.id.btnUpdateDoctor);
        btnDeleteDoctor = findViewById(R.id.btnDeleteDoctor);

        db = FirebaseFirestore.getInstance();

        doctorId = getIntent().getStringExtra("doctorId");

        etDoctorName.setText(getIntent().getStringExtra("name"));
        etSpecialization.setText(getIntent().getStringExtra("specialization"));
        etHospital.setText(getIntent().getStringExtra("hospital"));
        etExperience.setText(getIntent().getStringExtra("experience"));
        etFees.setText(getIntent().getStringExtra("fees"));
        etRating.setText(getIntent().getStringExtra("rating"));

        String availability = getIntent().getStringExtra("availability");

        if (availability != null) {
            ArrayAdapter adapter =
                    (ArrayAdapter) spinnerAvailability.getAdapter();

            int position = adapter.getPosition(availability);

            if (position >= 0) {
                spinnerAvailability.setSelection(position);
            }
        }

        btnUpdateDoctor.setOnClickListener(v -> updateDoctor());

        btnDeleteDoctor.setOnClickListener(v -> deleteDoctor());
    }

    private void updateDoctor() {

        String name = etDoctorName.getText().toString().trim();
        String specialization = etSpecialization.getText().toString().trim();
        String hospital = etHospital.getText().toString().trim();
        String experience = etExperience.getText().toString().trim();
        String fees = etFees.getText().toString().trim();
        String rating = etRating.getText().toString().trim();
        String availability = spinnerAvailability.getSelectedItem().toString();

        if (TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(specialization) ||
                TextUtils.isEmpty(hospital) ||
                TextUtils.isEmpty(experience) ||
                TextUtils.isEmpty(fees)) {

            Toast.makeText(this,
                    "Please fill all required fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> doctor = new HashMap<>();

        doctor.put("name", name);
        doctor.put("specialization", specialization);
        doctor.put("hospital", hospital);
        doctor.put("experience", experience);
        doctor.put("fees", fees);
        doctor.put("rating", rating);
        doctor.put("availability", availability);

        db.collection("doctors")
                .document(doctorId)
                .update(doctor)
                .addOnSuccessListener(unused -> {

                    Toast.makeText(this,
                            "Doctor Updated Successfully",
                            Toast.LENGTH_SHORT).show();

                    finish();

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT).show());
    }

    private void deleteDoctor() {

        new AlertDialog.Builder(this)
                .setTitle("Delete Doctor")
                .setMessage("Are you sure you want to delete this doctor?")
                .setPositiveButton("Yes", (dialog, which) ->

                        db.collection("doctors")
                                .document(doctorId)
                                .delete()
                                .addOnSuccessListener(unused -> {

                                    Toast.makeText(this,
                                            "Doctor Deleted",
                                            Toast.LENGTH_SHORT).show();

                                    finish();

                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(this,
                                                e.getMessage(),
                                                Toast.LENGTH_SHORT).show())

                )
                .setNegativeButton("No", null)
                .show();
    }
}