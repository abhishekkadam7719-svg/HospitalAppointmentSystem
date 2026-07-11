package com.example.hospitalappointmentsystem.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalappointmentsystem.ManageAppointmentsActivity;
import com.example.hospitalappointmentsystem.R;
import com.example.hospitalappointmentsystem.model.Appointment;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    Context context;
    ArrayList<Appointment> notificationList;

    public NotificationAdapter(Context context, ArrayList<Appointment> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_notification, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Appointment appointment = notificationList.get(position);

        holder.tvTitle.setText("New Appointment Request");

        holder.tvMessage.setText(
                appointment.getPatientName() +
                        " booked an appointment with " +
                        appointment.getDoctorName()
        );

        holder.tvTime.setText(
                appointment.getDate() + " • " + appointment.getTime()
        );

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, ManageAppointmentsActivity.class);
            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvMessage, tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}