package com.example.projet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskDetailsActivity extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText;
    private TextView dueDateTextView;
    private CheckBox statusCheckBox;
    private DBHelper dbHelper;
    private int taskId;
    private Button shareButton, setReminderButton, saveButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        // Initialize the views
        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dueDateTextView = findViewById(R.id.dueDateTextView);
        statusCheckBox = findViewById(R.id.statusCheckBox);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        shareButton = findViewById(R.id.shareButton);
        setReminderButton = findViewById(R.id.setReminderButton);

        // Initialize the database helper
        dbHelper = new DBHelper(this);

        // Retrieve data from the Intent
        Intent intent = getIntent();
        taskId = intent.getIntExtra("id", -1);
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String dueDate = intent.getStringExtra("dueDate");
        int status = intent.getIntExtra("status", 0);



// Log the task data for debugging

        Log.d("TaskDetailsActivity", "Task ID: " + taskId);

        Log.d("TaskDetailsActivity", "Task Title: " + title);

        Log.d("TaskDetailsActivity", "Task Description: " + description);

        Log.d("TaskDetailsActivity", "Task Due Date: " + dueDate);

        Log.d("TaskDetailsActivity", "Task Status: " + status);



// Populate the views with task data

        titleEditText.setText(title);
        descriptionEditText.setText(description);
        dueDateTextView.setText(dueDate);
        statusCheckBox.setChecked(status == 1);




// Set the click listener for the Save button
        saveButton.setOnClickListener(v -> {
            String updatedTitle = titleEditText.getText().toString().trim();
            String updatedDescription = descriptionEditText.getText().toString().trim();
            String updatedDueDateStr = dueDateTextView.getText().toString().trim();

            // Parse the updatedDueDate string using Java Time API
            java.time.LocalDateTime localDateTime = java.time.LocalDateTime.parse(updatedDueDateStr, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            long dueDateInMillis = localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();

            int updatedStatus = statusCheckBox.isChecked() ? 1 : 0;

            dbHelper.updateTask(taskId, updatedTitle, updatedDescription, updatedDueDateStr, updatedStatus);
            Toast.makeText(this, "Tâche mise à jour", Toast.LENGTH_SHORT).show();
            finish();
        });


// Set the click listener for the Delete button

        deleteButton.setOnClickListener(v -> {

// Delete the task from the database

            dbHelper.deleteTask(taskId);

            Toast.makeText(this, "Tâche supprimée", Toast.LENGTH_SHORT).show();



// Close the activity

            finish();

        });





        shareButton.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Titre de la tâche : " + title + "\nDescription : " + description);
            startActivity(Intent.createChooser(shareIntent, "Partager la tâche"));
        });

        setReminderButton.setOnClickListener(v -> {
            // Assuming updatedDueDate is already in the desired format (e.g., "2024-12-15 12:00:00")
            String updatedDueDate = dueDateTextView.getText().toString().trim();

            // Directly set the due date as event location
            Intent intent2 = new Intent(Intent.ACTION_EDIT);
            intent2.setType("vnd.android.cursor.item/event");
            intent2.putExtra("eventLocation", updatedDueDate); // Set due date as event location
            intent2.putExtra("title", title);
            startActivity(intent2);
        });
    }
}
