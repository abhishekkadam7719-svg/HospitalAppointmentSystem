package com.example.hospitalappointmentsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.hospitalappointmentsystem.adapter.NotificationAdapter;
import com.example.hospitalappointmentsystem.model.Appointment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView recyclerNotifications;
    SwipeRefreshLayout swipeRefresh;
    View emptyState;

    ImageButton btnBack;
    TextView tvClearAll;

    NotificationAdapter adapter;
    ArrayList<Appointment> notificationList;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerNotifications = findViewById(R.id.recyclerNotifications);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        emptyState = findViewById(R.id.emptyState);

        btnBack = findViewById(R.id.btnBack);
        tvClearAll = findViewById(R.id.tvClearAll);

        recyclerNotifications.setLayoutManager(new LinearLayoutManager(this));

        notificationList = new ArrayList<>();
        adapter = new NotificationAdapter(this, notificationList);
        recyclerNotifications.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadNotifications();

        btnBack.setOnClickListener(v -> finish());

        swipeRefresh.setOnRefreshListener(() -> {
            loadNotifications();
            swipeRefresh.setRefreshing(false);
        });

        tvClearAll.setOnClickListener(v -> {

            db.collection("appointments")
                    .whereEqualTo("status", "Pending")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            document.getReference().update("status", "Viewed");
                        }

                        loadNotifications();
                    });

        });
    }

    private void loadNotifications() {

        db.collection("appointments")
                .whereEqualTo("status", "Pending")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    notificationList.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                        Appointment appointment =
                                document.toObject(Appointment.class);

                        appointment.setId(document.getId());

                        notificationList.add(appointment);
                    }

                    adapter.notifyDataSetChanged();

                    if (notificationList.isEmpty()) {

                        emptyState.setVisibility(View.VISIBLE);
                        recyclerNotifications.setVisibility(View.GONE);

                    } else {

                        emptyState.setVisibility(View.GONE);
                        recyclerNotifications.setVisibility(View.VISIBLE);

                    }

                });
    }
}