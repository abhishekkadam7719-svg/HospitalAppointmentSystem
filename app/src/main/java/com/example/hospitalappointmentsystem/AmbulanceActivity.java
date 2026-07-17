package com.example.hospitalappointmentsystem;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AmbulanceActivity extends AppCompatActivity {

    private EditText etPatientName, etLocation;
    private Button btnCallAmbulance;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private DatabaseReference databaseReference;
    private String requestId;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance);

        etPatientName = findViewById(R.id.etPatientName);
        etLocation = findViewById(R.id.etLocation);
        btnCallAmbulance = findViewById(R.id.btnCallAmbulance);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateIntervalMillis(3000)
                .build();

        requestId = FirebaseDatabase.getInstance().getReference("ambulance_requests").push().getKey();
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("ambulance_requests")
                .child(requestId != null ? requestId : "default_request");

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;
                double lat = locationResult.getLastLocation().getLatitude();
                double lng = locationResult.getLastLocation().getLongitude();

                etLocation.setText(lat + ", " + lng);

                String name = etPatientName.getText().toString().trim();
                databaseReference.child("lat").setValue(lat);
                databaseReference.child("lng").setValue(lng);
                databaseReference.child("patientName").setValue(name.isEmpty() ? "Unknown" : name);
                databaseReference.child("status").setValue("live");
            }
        };

        btnCallAmbulance.setOnClickListener(v -> {
            String name = etPatientName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter patient name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (checkLocationPermission()) {
                startLocationUpdates();
                Toast.makeText(this, "Sharing live location...", Toast.LENGTH_SHORT).show();
            }

            String ambulanceNumber = "102";
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ambulanceNumber));
            startActivity(intent);
        });
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
                Toast.makeText(this, "Sharing live location...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location permission is required to track ambulance", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
}