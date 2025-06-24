package com.s22010342.firmfous;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ReminderListActivityOne extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReminderAdapter adapter;
    private List<Reminder> reminderList;
    private FirebaseFirestore db;
    private String userOrgCode; //  org code from login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_list_one);

        recyclerView = findViewById(R.id.reminderRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reminderList = new ArrayList<>();
        adapter = new ReminderAdapter(this, reminderList, userOrgCode);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadRemindersFromFirebase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userOrgCode = extras.getString("organization_code");
           Log.d("ReminderModuleActivity", "Global Org Code : " + userOrgCode);

        }

    }

    private void loadRemindersFromFirebase() {
        db.collection("organizations").document(userOrgCode)
                .collection("reminders")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    reminderList.clear();
                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        Reminder r = doc.toObject(Reminder.class);
                        if (r != null) {
                            r = new Reminder(doc.getId(), r.getTitle(), r.getDescription(),
                                    r.getDate(), r.getTime(), r.getLocation(),
                                    r.getImageUrl(), r.getStatus());
                            reminderList.add(r);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}