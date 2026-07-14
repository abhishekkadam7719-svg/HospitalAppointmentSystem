package com.example.hospitalappointmentsystem.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalappointmentsystem.AppointmentActivity;
import com.example.hospitalappointmentsystem.R;
import com.example.hospitalappointmentsystem.model.Doctor;

import java.util.ArrayList;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private ArrayList<Doctor> doctorList;

    public DoctorAdapter(ArrayList<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    public void filterList(ArrayList<Doctor> filteredList) {
        this.doctorList = filteredList;
        notifyDataSetChanged();
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
        holder.tvHospital.setText(doctor.getHospital());
        holder.tvExperience.setText("Experience: " + doctor.getExperience());
        holder.tvFees.setText("Consultation Fee: ₹" + doctor.getFees());
        holder.tvAvailability.setText(doctor.getAvailability());
        holder.tvRating.setText("⭐ " + doctor.getRating());

        String imageName = doctor.getImage();

        if (imageName != null && !imageName.isEmpty()) {

            int imageRes = holder.itemView.getContext().getResources()
                    .getIdentifier(imageName,
                            "drawable",
                            holder.itemView.getContext().getPackageName());

            if (imageRes != 0) {
                holder.imgDoctor.setImageResource(imageRes);
            } else {
                holder.imgDoctor.setImageResource(R.drawable.doctor1);
            }

        } else {

            holder.imgDoctor.setImageResource(R.drawable.doctor1);

        }

        holder.btnBook.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), AppointmentActivity.class);

            intent.putExtra("doctorName", doctor.getName());
            intent.putExtra("specialization", doctor.getSpecialization());
            intent.putExtra("hospital", doctor.getHospital());
            intent.putExtra("experience", doctor.getExperience());
            intent.putExtra("fees", doctor.getFees());
            intent.putExtra("rating", doctor.getRating());
            intent.putExtra("availability", doctor.getAvailability());
            intent.putExtra("doctorImage", doctor.getImage());

            v.getContext().startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return doctorList == null ? 0 : doctorList.size();
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder {

        ImageView imgDoctor;
        TextView tvDoctorName;
        TextView tvSpecialization;
        TextView tvHospital;
        TextView tvExperience;
        TextView tvFees;
        TextView tvAvailability;
        TextView tvRating;
        Button btnBook;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);

            imgDoctor = itemView.findViewById(R.id.imgDoctor);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvSpecialization = itemView.findViewById(R.id.tvSpecialization);
            tvHospital = itemView.findViewById(R.id.tvHospital);
            tvExperience = itemView.findViewById(R.id.tvExperience);
            tvFees = itemView.findViewById(R.id.tvFees);
            tvAvailability = itemView.findViewById(R.id.tvAvailability);
            tvRating = itemView.findViewById(R.id.tvRating);
            btnBook = itemView.findViewById(R.id.btnBook);
        }
    }
}