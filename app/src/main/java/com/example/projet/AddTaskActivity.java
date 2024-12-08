package com.example.projet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    EditText titleInput, descriptionInput;
    DBHelper dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        titleInput = findViewById(R.id.titleInput);
        descriptionInput = findViewById(R.id.descriptionInput);

        dbHelper = new DBHelper(this);

        findViewById(R.id.saveButton).setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();

            if (!title.isEmpty()) {
                dbHelper.insertTask(AddTaskActivity.this, title, description, "2024-11-26");

                Toast.makeText(AddTaskActivity.this, "Tâche ajoutée avec succès", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Toast.makeText(AddTaskActivity.this, "Le titre de la tâche est obligatoire", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
