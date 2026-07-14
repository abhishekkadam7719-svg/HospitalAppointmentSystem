package com.example.hospitalappointmentsystem.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalappointmentsystem.EditDoctorActivity;
import com.example.hospitalappointmentsystem.R;
import com.example.hospitalappointmentsystem.model.Doctor;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminDoctorAdapter extends RecyclerView.Adapter<AdminDoctorAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Doctor> doctorList;
    private final FirebaseFirestore db;

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
        holder.tvSpecialization.setText(doctor.getSpecialization());
        holder.tvHospital.setText(doctor.getHospital());
        holder.tvExperience.setText(doctor.getExperience());
        holder.tvFees.setText("₹" + doctor.getFees());

        if (doctor.getName() != null && !doctor.getName().isEmpty()) {
            holder.tvInitial.setText(
                    doctor.getName().substring(0, 1).toUpperCase()
            );
        } else {
            holder.tvInitial.setText("D");
        }

        // Edit Doctor
        holder.btnEdit.setOnClickListener(v -> {

            Intent intent = new Intent(context, EditDoctorActivity.class);

            intent.putExtra("doctorId", doctor.getDoctorId());
            intent.putExtra("name", doctor.getName());
            intent.putExtra("specialization", doctor.getSpecialization());
            intent.putExtra("hospital", doctor.getHospital());
            intent.putExtra("experience", doctor.getExperience());
            intent.putExtra("fees", doctor.getFees());
            intent.putExtra("rating", doctor.getRating());
            intent.putExtra("availability", doctor.getAvailability());

            context.startActivity(intent);

        });

        // Delete Doctor
        holder.btnDelete.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Delete Doctor")
                    .setMessage("Are you sure you want to delete this doctor?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        int pos = holder.getAdapterPosition();

                        if (pos == RecyclerView.NO_POSITION)
                            return;

                        db.collection("doctors")
                                .document(doctor.getDoctorId())
                                .delete()
                                .addOnSuccessListener(unused -> {

                                    doctorList.remove(pos);
                                    notifyItemRemoved(pos);

                                    Toast.makeText(context,
                                            "Doctor deleted successfully",
                                            Toast.LENGTH_SHORT).show();

                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(context,
                                                e.getMessage(),
                                                Toast.LENGTH_SHORT).show());

                    })
                    .setNegativeButton("No", null)
                    .show();

        });

    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvInitial;
        TextView tvDoctorName;
        TextView tvSpecialization;
        TextView tvHospital;
        TextView tvExperience;
        TextView tvFees;
        TextView btnEdit;
        TextView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvInitial = itemView.findViewById(R.id.tvInitial);
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