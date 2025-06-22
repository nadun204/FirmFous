package com.s22010342.firmfous;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    private Context context;
    private List<Reminder> reminderList;
    private FirebaseFirestore db;
    private String orgCode;

    public ReminderAdapter(Context context, List<Reminder> reminderList, String orgCode) {
        this.context = context;
        this.reminderList = reminderList;
        this.orgCode = orgCode;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reminder, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder reminder = reminderList.get(position);

        // Set text values
        holder.titleText.setText(reminder.getTitle());
        holder.descText.setText(reminder.getDescription());
        holder.dateTimeText.setText(reminder.getDate() + " " + reminder.getTime());
        holder.locationText.setText("Locstion " + reminder.getLocation());

        // Load image
        if (reminder.getImageUrl() != null && !reminder.getImageUrl().isEmpty()) {
            Glide.with(context).load(reminder.getImageUrl()).into(holder.reminderImage);
        }

        // Highlight based on status
        if ("completed".equalsIgnoreCase(reminder.getStatus())) {
            holder.itemView.setBackgroundColor(Color.parseColor("#C8E6C9")); // Green
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFF9C4")); // Yellow
        }

        // Mark as completed
        holder.markCompletedBtn.setOnClickListener(v -> {
            db.collection("organizations").document(orgCode)
                    .collection("reminders").document(reminder.getId())
                    .update("status", "completed")
                    .addOnSuccessListener(unused -> {
                        reminder.setStatus("completed");
                        notifyItemChanged(position);
                        Toast.makeText(context, "Marked as completed", Toast.LENGTH_SHORT).show();
                    });
        });

        // Delete reminder
        holder.deleteBtn.setOnClickListener(v -> {
            db.collection("organizations").document(orgCode)
                    .collection("reminders").document(reminder.getId())
                    .delete()
                    .addOnSuccessListener(unused -> {
                        reminderList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Reminder deleted", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, descText, dateTimeText, locationText;
        ImageView reminderImage;
        Button markCompletedBtn, deleteBtn;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            descText = itemView.findViewById(R.id.descText);
            dateTimeText = itemView.findViewById(R.id.dateTimeText);
            locationText = itemView.findViewById(R.id.locationText);
            reminderImage = itemView.findViewById(R.id.reminderImage);
            markCompletedBtn = itemView.findViewById(R.id.markCompletedBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
