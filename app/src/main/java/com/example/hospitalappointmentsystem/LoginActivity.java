package com.example.hospitalappointmentsystem;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText etEmail, etPassword;
    Button btnLogin;
    TextView tvRegister, tvForgot;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgot = findViewById(R.id.tvForgot);

        btnLogin.setOnClickListener(v -> loginUser());

        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void loginUser() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Enter Email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Enter Password");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Toast.makeText(LoginActivity.this,
                                "Login Successful",
                                Toast.LENGTH_SHORT).show();

                        String userEmail = FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .getEmail();

                        // Change this email to your admin email
                        if (userEmail.equalsIgnoreCase("admin@gmail.com")) {

                            startActivity(new Intent(
                                    LoginActivity.this,
                                    AdminDashboardActivity.class));

                        } else {

                            startActivity(new Intent(
                                    LoginActivity.this,
                                    HomeActivity.class));
                        }

                        finish();

                    } else {

                        Toast.makeText(LoginActivity.this,
                                task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}