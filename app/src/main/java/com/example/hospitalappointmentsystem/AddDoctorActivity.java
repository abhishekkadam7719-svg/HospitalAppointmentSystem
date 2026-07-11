package com.example.hospitalappointmentsystem;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hospitalappointmentsystem.model.Doctor;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddDoctorActivity extends AppCompatActivity {

    TextInputEditText etDoctorName, etSpecialization, etHospital,
            etExperience, etFee, etPhone, etImage;

    Button btnSaveDoctor;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);

        db = FirebaseFirestore.getInstance();

        etDoctorName = findViewById(R.id.etDoctorName);
        etSpecialization = findViewById(R.id.etSpecialization);
        etHospital = findViewById(R.id.etHospital);
        etExperience = findViewById(R.id.etExperience);
        etFee = findViewById(R.id.etFee);
        etPhone = findViewById(R.id.etPhone);
        etImage = findViewById(R.id.etImage);

        btnSaveDoctor = findViewById(R.id.btnSaveDoctor);

        btnSaveDoctor.setOnClickListener(v -> saveDoctor());
    }

    private void saveDoctor() {

        String name = etDoctorName.getText().toString().trim();
        String specialization = etSpecialization.getText().toString().trim();
        String hospital = etHospital.getText().toString().trim();
        String experience = etExperience.getText().toString().trim();
        String fee = etFee.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String image = etImage.getText().toString().trim();

        if (TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(specialization) ||
                TextUtils.isEmpty(hospital) ||
                TextUtils.isEmpty(experience) ||
                TextUtils.isEmpty(fee) ||
                TextUtils.isEmpty(phone)) {

            Toast.makeText(this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Doctor doctor = new Doctor(
                name,
                specialization,
                hospital,
                experience,
                fee,
                phone,
                image
        );

        db.collection("doctors")
                .add(doctor)
                .addOnSuccessListener(documentReference -> {

                    Toast.makeText(this,
                            "Doctor Added Successfully",
                            Toast.LENGTH_SHORT).show();

                    finish();

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                e.getMessage(),
                                Toast.LENGTH_LONG).show());

    }
}