package com.example.hospitalappointmentsystem.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalappointmentsystem.AddDoctorActivity;
import com.example.hospitalappointmentsystem.R;
import com.example.hospitalappointmentsystem.model.Doctor;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminDoctorAdapter extends RecyclerView.Adapter<AdminDoctorAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Doctor> doctorList;
    private FirebaseFirestore db;

    public AdminDoctorAdapter(Context context, ArrayList<Doctor> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.doctor_admin_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Doctor doctor = doctorList.get(position);

        holder.tvDoctorName.setText(doctor.getName());
        holder.tvSpecialization.setText("Specialization: " + doctor.getSpecialization());
        holder.tvHospital.setText("Hospital: " + doctor.getHospital());
        holder.tvExperience.setText("Experience: " + doctor.getExperience());
        holder.tvFees.setText("Fees: ₹" + doctor.getFees());

        holder.btnDelete.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Delete Doctor")
                    .setMessage("Are you sure you want to delete this doctor?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        db.collection("doctors")
                                .document(doctor.getDoctorId())
                                .delete()
                                .addOnSuccessListener(unused -> {

                                    doctorList.remove(position);
                                    notifyItemRemoved(position);

                                    Toast.makeText(context,
                                            "Doctor Deleted",
                                            Toast.LENGTH_SHORT).show();

                                });

                    })
                    .setNegativeButton("No", null)
                    .show();

        });

        holder.btnEdit.setOnClickListener(v -> {

            Toast.makeText(context,
                    "Edit feature will be added next.",
                    Toast.LENGTH_SHORT).show();

        });

    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDoctorName, tvSpecialization, tvHospital, tvExperience, tvFees;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvSpecialization = itemView.findViewById(R.id.tvSpecialization);
            tvHospital = itemView.findViewById(R.id.tvHospital);
            tvExperience = itemView.findViewById(R.id.tvExperience);
            tvFees = itemView.findViewById(R.id.tvFees);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}