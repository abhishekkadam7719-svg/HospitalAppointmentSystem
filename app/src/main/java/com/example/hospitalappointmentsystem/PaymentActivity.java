package com.example.hospitalappointmentsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvDoctorName, tvFees, tvTotal, tvConsultationFee, tvPlatformFee;
    private RadioButton rbUPI, rbCard, rbWallet, rbNetBanking;
    private LinearLayout rowUPI, rowCard, rowWallet, rowNetBanking;
    private Button btnPayNow;

    private FirebaseFirestore db;

    private String doctorName, specialization, fees;
    private String patientName, phone, symptoms;
    private String date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        tvDoctorName = findViewById(R.id.tvDoctorName);
        tvFees = findViewById(R.id.tvFees);
        tvConsultationFee = findViewById(R.id.tvConsultationFee);
        tvPlatformFee = findViewById(R.id.tvPlatformFee);
        tvTotal = findViewById(R.id.tvTotal);

        rbUPI = findViewById(R.id.rbUPI);
        rbCard = findViewById(R.id.rbCard);
        rbWallet = findViewById(R.id.rbWallet);
        rbNetBanking = findViewById(R.id.rbNetBanking);

        rowUPI = findViewById(R.id.rowUPI);
        rowCard = findViewById(R.id.rowCard);
        rowWallet = findViewById(R.id.rowWallet);
        rowNetBanking = findViewById(R.id.rowNetBanking);

        btnPayNow = findViewById(R.id.btnPayNow);

        db = FirebaseFirestore.getInstance();

        // Receive data
        Intent intent = getIntent();

        doctorName = intent.getStringExtra("doctorName");
        specialization = intent.getStringExtra("specialization");
        fees = intent.getStringExtra("fees");
        patientName = intent.getStringExtra("patientName");
        phone = intent.getStringExtra("phone");
        symptoms = intent.getStringExtra("symptoms");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");

        if (fees == null || fees.isEmpty()) {
            fees = "0";
        }

        tvDoctorName.setText(doctorName);
        tvFees.setText("Consultation Fee: ₹" + fees);
        tvConsultationFee.setText("₹" + fees);
        tvPlatformFee.setText("₹0");
        tvTotal.setText("₹" + fees);

        // Manual radio group behavior since RadioButtons are nested inside cards
        final RadioButton[] allRadios = {rbUPI, rbCard, rbWallet, rbNetBanking};

        View.OnClickListener rowClickListener = v -> {
            rbUPI.setChecked(v == rowUPI);
            rbCard.setChecked(v == rowCard);
            rbWallet.setChecked(v == rowWallet);
            rbNetBanking.setChecked(v == rowNetBanking);
        };

        rowUPI.setOnClickListener(rowClickListener);
        rowCard.setOnClickListener(rowClickListener);
        rowWallet.setOnClickListener(rowClickListener);
        rowNetBanking.setOnClickListener(rowClickListener);

        btnPayNow.setOnClickListener(v -> {

            if (!rbUPI.isChecked() &&
                    !rbCard.isChecked() &&
                    !rbWallet.isChecked() &&
                    !rbNetBanking.isChecked()) {

                Toast.makeText(PaymentActivity.this,
                        "Please select a payment method",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String paymentMethod;

            if (rbUPI.isChecked()) {
                paymentMethod = "UPI";
            } else if (rbCard.isChecked()) {
                paymentMethod = "Card";
            } else if (rbWallet.isChecked()) {
                paymentMethod = "Wallet";
            } else {
                paymentMethod = "Net Banking";
            }

            HashMap<String, Object> appointment = new HashMap<>();

            appointment.put("doctorName", doctorName);
            appointment.put("specialization", specialization);
            appointment.put("fees", fees);
            appointment.put("patientName", patientName);
            appointment.put("phone", phone);
            appointment.put("symptoms", symptoms);
            appointment.put("date", date);
            appointment.put("time", time);
            appointment.put("paymentMethod", paymentMethod);
            appointment.put("paymentStatus", "Paid");
            appointment.put("status", "Confirmed");

            db.collection("appointments")
                    .add(appointment)
                    .addOnSuccessListener(documentReference -> {

                        Toast.makeText(PaymentActivity.this,
                                "Payment Successful!\nAppointment Booked.",
                                Toast.LENGTH_LONG).show();

                        Intent i = new Intent(PaymentActivity.this,
                                MyAppointmentsActivity.class);

                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();

                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(PaymentActivity.this,
                                    e.getMessage(),
                                    Toast.LENGTH_LONG).show());
        });
    }
}