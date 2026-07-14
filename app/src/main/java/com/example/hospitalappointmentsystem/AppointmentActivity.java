package com.example.hospitalappointmentsystem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class AppointmentActivity extends AppCompatActivity {

    ImageView imgDoctor;
    TextView txtDoctorName, txtSpecialization, txtFees;
    EditText etPatientName, etPhone, etSymptoms;
    Button btnConfirm, btnSelectDate, btnSelectTime;

    String selectedDate = "";
    String selectedTime = "";

    String doctorName;
    String specialization;
    String fees;
    String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            v.setPadding(0, top, 0, 0);
            return insets;
        });

        imgDoctor = findViewById(R.id.imgDoctor);
        txtDoctorName = findViewById(R.id.txtDoctorName);
        txtSpecialization = findViewById(R.id.txtSpecialization);
        txtFees = findViewById(R.id.txtFees);

        etPatientName = findViewById(R.id.etPatientName);
        etPhone = findViewById(R.id.etPhone);
        etSymptoms = findViewById(R.id.etSymptoms);

        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        btnConfirm = findViewById(R.id.btnConfirm);

        // Receive doctor data
        doctorName = getIntent().getStringExtra("doctorName");
        specialization = getIntent().getStringExtra("specialization");
        fees = getIntent().getStringExtra("fees");
        imageName = getIntent().getStringExtra("doctorImage");

        txtDoctorName.setText(doctorName);
        txtSpecialization.setText(specialization);
        txtFees.setText("Consultation Fee: ₹" + fees);

        int imageRes = getResources().getIdentifier(imageName, "drawable", getPackageName());

        if (imageRes != 0) {
            imgDoctor.setImageResource(imageRes);
        } else {
            imgDoctor.setImageResource(R.drawable.doctor1);
        }

        // Date Picker
        btnSelectDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            DatePickerDialog dialog = new DatePickerDialog(
                    AppointmentActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        btnSelectDate.setText(selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            dialog.show();
        });

        // Time Picker
        btnSelectTime.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            TimePickerDialog dialog = new TimePickerDialog(
                    AppointmentActivity.this,
                    (view, hourOfDay, minute) -> {
                        selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        btnSelectTime.setText(selectedTime);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
            );

            dialog.show();
        });

        // Continue to Payment
        btnConfirm.setOnClickListener(v -> {

            String patientName = etPatientName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String symptoms = etSymptoms.getText().toString().trim();

            if (patientName.isEmpty() ||
                    phone.isEmpty() ||
                    symptoms.isEmpty() ||
                    selectedDate.isEmpty() ||
                    selectedTime.isEmpty()) {

                Toast.makeText(this,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(AppointmentActivity.this, PaymentActivity.class);

            intent.putExtra("doctorName", doctorName);
            intent.putExtra("specialization", specialization);
            intent.putExtra("fees", fees);
            intent.putExtra("doctorImage", imageName);

            intent.putExtra("patientName", patientName);
            intent.putExtra("phone", phone);
            intent.putExtra("symptoms", symptoms);

            intent.putExtra("date", selectedDate);
            intent.putExtra("time", selectedTime);

            startActivity(intent);
        });

    }
}