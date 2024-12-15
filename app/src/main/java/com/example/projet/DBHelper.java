package com.example.projet;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE = "TaskManager.db";
    public static final String TABLE = "tasks";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String DUE_DATE = "due_date";
    public static final String STATUS = "status";

    public DBHelper(Context context) {

        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " TEXT NOT NULL, " +
                DESCRIPTION + " TEXT, " +
                DUE_DATE + " TEXT, " +
                STATUS + " INTEGER DEFAULT 0)");
    }




    public void insertTask(AddTaskActivity addTaskActivity, String title, String description, String dueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, title);
        values.put(DESCRIPTION, description);
        values.put(DUE_DATE, dueDate);
        values.put(STATUS, 0);

        long rowId = db.insert(TABLE, null, values);
        Log.d("DBHelper", "Inserted task with ID: " + rowId);
        db.close();
    }


    public ArrayList<Task> getAllTasksAsList() {
        ArrayList<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(TITLE));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
                @SuppressLint("Range") String dueDate = cursor.getString(cursor.getColumnIndex(DUE_DATE));
                @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(STATUS));

                Task task = new Task(id, title, description, dueDate, status);
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return taskList;
    }



    public void updateTaskStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATUS, status);
        db.update(TABLE, values, ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void updateTask(int id, String title, String description, String dueDate, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, title);
        values.put(DESCRIPTION, description);
        values.put(DUE_DATE, dueDate);
        values.put(STATUS, status);

        db.update(TABLE, values, ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

}
