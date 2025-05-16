package com.example.jutai;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyListings extends AppCompatActivity {

    RecyclerView recyclerView;
    EquipmentAdapter adapter;
    List<EquipmentModel> equipmentList;
    DatabaseHelper dbHelper;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings); // make sure this XML file has a RecyclerView with id recyclerView

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = prefs.getInt("UserId", -1);

        if (userId != -1) {
            equipmentList = dbHelper.getEquipmentsByOwner(userId);
            if (equipmentList.isEmpty()) {
                Toast.makeText(this, "You have no listings yet!", Toast.LENGTH_SHORT).show();
            } else {

                recyclerView.setAdapter(new EquipmentAdapter(this, equipmentList, "owner"));
            }
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
        }
    }
}
