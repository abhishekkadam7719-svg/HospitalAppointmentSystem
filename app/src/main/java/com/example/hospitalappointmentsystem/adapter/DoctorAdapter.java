package com.example.hospitalappointmentsystem.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalappointmentsystem.R;
import com.example.hospitalappointmentsystem.model.Doctor;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private List<Doctor> doctorList;

    public DoctorAdapter(List<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_item, parent, false);

        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {

        Doctor doctor = doctorList.get(position);

        holder.tvDoctorName.setText(doctor.getName());
        holder.tvSpecialization.setText(doctor.getSpecialization());
        holder.tvExperience.setText("Experience: " + doctor.getExperience());
        holder.tvFees.setText("Consultation Fee: ₹" + doctor.getFees());
        holder.tvAvailability.setText(doctor.getAvailability());

        // We will load the doctor's image from Firebase Storage later.
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder {

        ImageView imgDoctor;
        TextView tvDoctorName, tvSpecialization, tvExperience, tvFees, tvAvailability;
        Button btnBook;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);

            imgDoctor = itemView.findViewById(R.id.imgDoctor);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvSpecialization = itemView.findViewById(R.id.tvSpecialization);
            tvExperience = itemView.findViewById(R.id.tvExperience);
            tvFees = itemView.findViewById(R.id.tvFees);
            tvAvailability = itemView.findViewById(R.id.tvAvailability);
            btnBook = itemView.findViewById(R.id.btnBook);
        }
    }
}