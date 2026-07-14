package com.example.hospitalappointmentsystem.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.widget.LinearLayout;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalappointmentsystem.R;
import com.example.hospitalappointmentsystem.model.Appointment;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private List<Appointment> appointmentList;

    public AppointmentAdapter(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Appointment appointment = appointmentList.get(position);

        switch (appointment.getStatus()) {
            case "Approved":
                holder.statusBadge.setBackgroundResource(R.drawable.bg_status_approved);
                holder.tvStatus.setTextColor(Color.parseColor("#2E9E5B"));
                holder.tvStatus.setText("Approved");
                break;
            case "Rejected":
                holder.statusBadge.setBackgroundResource(R.drawable.bg_status_rejected);
                holder.tvStatus.setTextColor(Color.parseColor("#E53935"));
                holder.tvStatus.setText("Rejected");
                break;
            default:
                holder.statusBadge.setBackgroundResource(R.drawable.bg_status_pending);
                holder.tvStatus.setTextColor(Color.parseColor("#F57C00"));
                holder.tvStatus.setText("Pending");
        }

        holder.tvDoctorName.setText(appointment.getDoctorName());
        holder.tvSpecialization.setText(appointment.getSpecialization());
        holder.tvDate.setText(appointment.getDate());
        holder.tvTime.setText(appointment.getTime());
        holder.tvPatient.setText(appointment.getPatientName());
        holder.tvPhone.setText(appointment.getPhone());
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout statusBadge;
        TextView tvDoctorName, tvSpecialization, tvDate,
                tvTime, tvPatient, tvPhone, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            statusBadge = itemView.findViewById(R.id.statusBadge);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvSpecialization = itemView.findViewById(R.id.tvSpecialization);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvPatient = itemView.findViewById(R.id.tvPatient);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}