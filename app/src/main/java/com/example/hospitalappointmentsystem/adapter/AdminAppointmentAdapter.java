package com.example.hospitalappointmentsystem.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalappointmentsystem.R;
import com.example.hospitalappointmentsystem.model.Appointment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminAppointmentAdapter extends RecyclerView.Adapter<AdminAppointmentAdapter.ViewHolder> {

    Context context;
    ArrayList<Appointment> appointmentList;
    FirebaseFirestore db;

    public AdminAppointmentAdapter(Context context, ArrayList<Appointment> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.admin_appointment_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Appointment appointment = appointmentList.get(position);

        holder.tvDoctorName.setText(appointment.getDoctorName());
        holder.tvPatientName.setText("Patient: " + appointment.getPatientName());
        holder.tvDate.setText("Date: " + appointment.getDate());
        holder.tvTime.setText("Time: " + appointment.getTime());
        holder.tvPhone.setText("Phone: " + appointment.getPhone());
        holder.tvStatus.setText("Status: " + appointment.getStatus());

        // Update buttons according to status
        if ("Approved".equalsIgnoreCase(appointment.getStatus())) {
            holder.btnApprove.setText("Approved");
            holder.btnApprove.setEnabled(false);
        } else {
            holder.btnApprove.setText("Approve");
            holder.btnApprove.setEnabled(true);
        }

        if ("Rejected".equalsIgnoreCase(appointment.getStatus())) {
            holder.btnReject.setText("Rejected");
            holder.btnReject.setEnabled(false);
        } else {
            holder.btnReject.setText("Reject");
            holder.btnReject.setEnabled(true);
        }

        // Approve Appointment
        holder.btnApprove.setOnClickListener(v -> {

            db.collection("appointments")
                    .document(appointment.getId())
                    .update("status", "Approved")
                    .addOnSuccessListener(unused -> {

                        appointment.setStatus("Approved");
                        notifyItemChanged(holder.getAdapterPosition());

                        Toast.makeText(context,
                                "Appointment Approved",
                                Toast.LENGTH_SHORT).show();
                    });
        });

        // Reject Appointment
        holder.btnReject.setOnClickListener(v -> {

            db.collection("appointments")
                    .document(appointment.getId())
                    .update("status", "Rejected")
                    .addOnSuccessListener(unused -> {

                        appointment.setStatus("Rejected");
                        notifyItemChanged(holder.getAdapterPosition());

                        Toast.makeText(context,
                                "Appointment Rejected",
                                Toast.LENGTH_SHORT).show();
                    });
        });

        // Delete Appointment
        holder.btnDelete.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Delete Appointment")
                    .setMessage("Are you sure you want to delete this appointment?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        db.collection("appointments")
                                .document(appointment.getId())
                                .delete()
                                .addOnSuccessListener(unused -> {

                                    int adapterPosition = holder.getAdapterPosition();
                                    if (adapterPosition != RecyclerView.NO_POSITION) {
                                        appointmentList.remove(adapterPosition);
                                        notifyItemRemoved(adapterPosition);
                                    }

                                    Toast.makeText(context,
                                            "Appointment Deleted",
                                            Toast.LENGTH_SHORT).show();
                                });

                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDoctorName, tvPatientName, tvDate, tvTime, tvPhone, tvStatus;
        Button btnApprove, btnReject, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvStatus = itemView.findViewById(R.id.tvStatus);

            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}