package com.example.hospitalappointmentsystem;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.UploadCallback;
import java.util.Map;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hospitalappointmentsystem.model.Doctor;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddDoctorActivity extends AppCompatActivity {

    private TextInputEditText etDoctorName, etSpecialization,
            etHospital, etExperience, etFee,
            etRating, etPhone;

    private ImageView imgDoctor;
    private Button btnSelectImage, btnSaveDoctor;
    private Spinner spinnerAvailability;

    private FirebaseFirestore db;

    private Uri imageUri;
    private String imageUrl = "";

    ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() == RESULT_OK
                                && result.getData() != null) {

                            imageUri = result.getData().getData();
                            imgDoctor.setImageURI(imageUri);
                            uploadImageToCloudinary(imageUri);

                            // Upload to Cloudinary here
                            // uploadImageToCloudinary(imageUri);

                        }

                    });

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

        imgDoctor = findViewById(R.id.imgDoctor);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSaveDoctor = findViewById(R.id.btnSaveDoctor);

        spinnerAvailability = findViewById(R.id.spinnerAvailability);

        db = FirebaseFirestore.getInstance();

        btnSelectImage.setOnClickListener(v -> {

            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            imagePickerLauncher.launch(intent);

        });

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
        doctor.setImage(imageUrl);
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
    private void uploadImageToCloudinary(Uri uri) {

        Toast.makeText(this, "Uploading Image...", Toast.LENGTH_SHORT).show();

        MediaManager.get().upload(uri)
                .callback(new UploadCallback() {

                    @Override
                    public void onStart(String requestId) {

                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {

                        imageUrl = resultData.get("secure_url").toString();

                        Toast.makeText(AddDoctorActivity.this,
                                "Image Uploaded Successfully",
                                Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(String requestId, com.cloudinary.android.callback.ErrorInfo error) {

                        Toast.makeText(AddDoctorActivity.this,
                                "Upload Failed",
                                Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onReschedule(String requestId, com.cloudinary.android.callback.ErrorInfo error) {

                    }

                })
                .dispatch();

    }
}