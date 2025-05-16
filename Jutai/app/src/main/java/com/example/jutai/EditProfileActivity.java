package com.example.jutai;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    EditText etName, etLocation, etOldPassword, etNewPassword;
    Button btnSaveChanges;
    DatabaseHelper dbHelper;
    SharedPreferences sharedPreferences;

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etName = findViewById(R.id.etName);
        etLocation = findViewById(R.id.etLocation);
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("UserId", -1);

        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadUserData();

        btnSaveChanges.setOnClickListener(v -> updateProfile());
    }

    private void loadUserData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name, location FROM users WHERE user_id = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            etName.setText(cursor.getString(0));
            etLocation.setText(cursor.getString(1));
        }
        cursor.close();
        db.close();
    }

    private void updateProfile() {
        String name = etName.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String oldPassword = etOldPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();

        if (name.isEmpty() || location.isEmpty() || oldPassword.isEmpty()) {
            Toast.makeText(this, "Name, location, and old password are required", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success;
        if (!newPassword.isEmpty()) {
            success = dbHelper.updateProfile(userId, name, location, oldPassword, newPassword);
        } else {
            success = dbHelper.updateProfile(userId, name, location, oldPassword, null);
        }

        if (success) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Incorrect old password", Toast.LENGTH_SHORT).show();
        }
    }
}
