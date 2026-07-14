package com.example.hospitalappointmentsystem;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hospitalappointmentsystem.model.Doctor;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddDoctorActivity extends AppCompatActivity {

    private TextInputEditText etDoctorName, etSpecialization,
            etHospital, etExperience, etFee,
            etRating, etPhone, etImage;

    private Spinner spinnerAvailability;
    private Button btnSaveDoctor;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);

        etDoctorName = findViewById(R.id.etDoctorName);
        etSpecialization = findViewById(R.id.etSpecialization);
        etHospital = findViewById(R.id.etHospital);
        etExperience = findViewById(R.id.etExperience);
        etFee = findViewById(R.id.etFee);
        etRating = findViewById(R.id.etRating);
        etPhone = findViewById(R.id.etPhone);
        etImage = findViewById(R.id.etImage);

        spinnerAvailability = findViewById(R.id.spinnerAvailability);
        btnSaveDoctor = findViewById(R.id.btnSaveDoctor);

        db = FirebaseFirestore.getInstance();

        btnSaveDoctor.setOnClickListener(v -> saveDoctor());
    }

    private void saveDoctor() {

        String name = etDoctorName.getText().toString().trim();
        String specialization = etSpecialization.getText().toString().trim();
        String hospital = etHospital.getText().toString().trim();
        String experience = etExperience.getText().toString().trim();
        String fee = etFee.getText().toString().trim();
        String rating = etRating.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String image = etImage.getText().toString().trim();
        String availability = spinnerAvailability.getSelectedItem().toString();

        if (TextUtils.isEmpty(name)
                || TextUtils.isEmpty(specialization)
                || TextUtils.isEmpty(hospital)
                || TextUtils.isEmpty(experience)
                || TextUtils.isEmpty(fee)
                || TextUtils.isEmpty(phone)) {

            Toast.makeText(this,
                    "Please fill all required fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Doctor doctor = new Doctor();

        doctor.setName(name);
        doctor.setSpecialization(specialization);
        doctor.setHospital(hospital);
        doctor.setExperience(experience);
        doctor.setFees(fee);
        doctor.setRating(rating);
        doctor.setPhone(phone);
        doctor.setImage(image);
        doctor.setAvailability(availability);

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
                                Toast.LENGTH_SHORT).show());
    }
}