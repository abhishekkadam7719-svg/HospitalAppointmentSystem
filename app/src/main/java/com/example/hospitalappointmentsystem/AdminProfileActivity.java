package com.example.hospitalappointmentsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etEmail, etPhone, etDob, etBloodGroup, etAddress;
    private Spinner spinnerGender;
    private Button btnSaveProfile, btnLogout;
    private ImageView imgProfile;
    private TextView tvChangePhoto;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        tvChangePhoto = findViewById(R.id.tvChangePhoto);

        // Init views
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etEmail.setEnabled(false);
        etPhone = findViewById(R.id.etPhone);
        etDob = findViewById(R.id.etDob);
        etBloodGroup = findViewById(R.id.etBloodGroup);
        etAddress = findViewById(R.id.etAddress);
        spinnerGender = findViewById(R.id.spinnerGender);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnLogout = findViewById(R.id.btnLogout);
        imgProfile = findViewById(R.id.imgProfile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Not logged in, send back to login
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        uid = currentUser.getUid();

        loadProfileData();

        etDob.setOnClickListener(v -> showDatePicker());

        btnSaveProfile.setOnClickListener(v -> saveProfileData());

        btnLogout.setOnClickListener(v -> logoutUser());

        imgProfile.setOnClickListener(v ->
                Toast.makeText(this, "Profile photo feature coming next", Toast.LENGTH_SHORT).show());

        tvChangePhoto.setOnClickListener(v ->
                imgProfile.performClick());
    }

    private void loadProfileData() {
        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        etFullName.setText(documentSnapshot.getString("fullName"));
                        etEmail.setText(documentSnapshot.getString("email"));
                        etPhone.setText(documentSnapshot.getString("phone"));
                        etDob.setText(documentSnapshot.getString("dob"));
                        etBloodGroup.setText(documentSnapshot.getString("bloodGroup"));
                        etAddress.setText(documentSnapshot.getString("address"));

                        String gender = documentSnapshot.getString("gender");
                        ArrayAdapter adapter = (ArrayAdapter) spinnerGender.getAdapter();
                        if (adapter != null && gender != null) {
                            int position = adapter.getPosition(gender);
                            if (position >= 0) spinnerGender.setSelection(position);
                        }
                    } else {
                        // No profile yet — prefill email from auth account
                        etEmail.setText(mAuth.getCurrentUser().getEmail());
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    etDob.setText(sdf.format(selected.getTime()));
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void saveProfileData() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem() != null ? spinnerGender.getSelectedItem().toString() : "";
        String bloodGroup = etBloodGroup.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (fullName.isEmpty()) {
            etFullName.setError("Enter Full Name");
            etFullName.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            etPhone.setError("Enter Phone Number");
            etPhone.requestFocus();
            return;
        }

        if (phone.length() != 10) {
            etPhone.setError("Enter a valid 10-digit phone number");
            etPhone.requestFocus();
            return;
        }

        Map<String, Object> profile = new HashMap<>();
        profile.put("fullName", fullName);
        profile.put("email", email);
        profile.put("phone", phone);
        profile.put("dob", dob);
        profile.put("gender", gender);
        profile.put("bloodGroup", bloodGroup);
        profile.put("address", address);

        db.collection("users").document(uid)
                .set(profile)
                .addOnSuccessListener(unused ->
                        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void logoutUser() {
        mAuth.signOut();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}