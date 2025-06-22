package com.s22010342.firmfous;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.*;

public class AddReminderActivity extends AppCompatActivity {

    EditText titleInput, descInput, locationInput;
    TextView dateInput, timeInput,showOrgCode;
    ImageView imageView;
    Button chooseImageBtn, saveReminderBtn;

    Uri imageUri;
    String orgCode; // Get this from login/session

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        titleInput = findViewById(R.id.titleInput);
        descInput = findViewById(R.id.descInput);
        locationInput = findViewById(R.id.locationInput);
        dateInput = findViewById(R.id.dateInput);
        timeInput = findViewById(R.id.timeInput);
        imageView = findViewById(R.id.imageView);
        chooseImageBtn = findViewById(R.id.chooseImageBtn);
        saveReminderBtn = findViewById(R.id.saveReminderBtn);
        showOrgCode = findViewById(R.id.showOrg);

        dateInput.setOnClickListener(v -> showDatePicker());
        timeInput.setOnClickListener(v -> showTimePicker());

        //  Get the Bundle of extras from the Intent that started this activity
        Bundle extras = getIntent().getExtras();
        String orgCodeFromLogin = extras.getString("organization_code");
        orgCode =orgCodeFromLogin; // asign user org code to variable
        showOrgCode.setText("Organization: " + orgCodeFromLogin);



        chooseImageBtn.setOnClickListener(v -> {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(i, "Select Picture"), 101);
        });

        saveReminderBtn.setOnClickListener(v -> saveReminder());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            dateInput.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            timeInput.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    void saveReminder() {
        // --- Step 1: Validate all required text fields ---
        if (orgCode == null || orgCode.isEmpty()) {
            Toast.makeText(this, "Error: Organization Code not found. Please log in again.", Toast.LENGTH_LONG).show();
            return;
        }

        String title = titleInput.getText().toString().trim();
        if (title.isEmpty()) {
            Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Saving reminder...", Toast.LENGTH_SHORT).show();
        saveReminderBtn.setEnabled(false);

        // --- Step 2: Decide which path to take based on imageUri ---
        if (imageUri != null) {
            // Path A: An image was selected. Upload it first.
            uploadImageThenSaveData();
        } else {
            // Path B: No image was selected. Save data directly with an empty URL.
            saveDataToFirestore("");
        }
    }

    // This method handles the image upload process
    private void uploadImageThenSaveData() {
        // Create a unique path for the image in Firebase Storage
        StorageReference ref = FirebaseStorage.getInstance().getReference("reminders/" + orgCode + "/" + UUID.randomUUID().toString());

        ref.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image upload was successful, now get its public URL
                    ref.getDownloadUrl().addOnSuccessListener(uri -> {
                        // With the URL, call the method to save everything to Firestore
                        saveDataToFirestore(uri.toString());
                    });
                })
                .addOnFailureListener(e -> {
                    // If image upload fails, show an error and re-enable the button
                    Toast.makeText(AddReminderActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    saveReminderBtn.setEnabled(true);
                });
    }

    // This single method handles saving data to Firestore, accepting an imageUrl
    private void saveDataToFirestore(String imageUrl) {
        // Get all text field values
        String title = titleInput.getText().toString().trim();
        String desc = descInput.getText().toString().trim();
        String location = locationInput.getText().toString().trim();
        String date = dateInput.getText().toString();
        String time = timeInput.getText().toString();

        // Create a Map object to hold the data
        Map<String, Object> reminder = new HashMap<>();
        reminder.put("title", title);
        reminder.put("description", desc);
        reminder.put("location", location);
        reminder.put("date", date);
        reminder.put("time", time);
        reminder.put("imageUrl", imageUrl); // This will be the REAL URL or an EMPTY STRING ""
        reminder.put("status", "ongoing");
        reminder.put("timestamp", System.currentTimeMillis());

        // Save the Map to Firestore
        FirebaseFirestore.getInstance().collection("organizations")
                .document(orgCode)
                .collection("reminders")
                .add(reminder)
                .addOnSuccessListener(docRef -> {
                    Toast.makeText(this, "Reminder saved successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity and go back
                })
                .addOnFailureListener(e -> {
                    // If saving to Firestore fails, show an error and re-enable the button
                    Toast.makeText(AddReminderActivity.this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    saveReminderBtn.setEnabled(true);
                });
    }

}
