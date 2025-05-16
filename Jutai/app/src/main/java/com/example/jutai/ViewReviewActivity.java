package com.example.jutai;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ViewReviewActivity extends AppCompatActivity {
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_review);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lv = findViewById(R.id.reviewList);

        int equipmentId = getIntent().getIntExtra("equipmentId", -1);
        if (equipmentId != -1) {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            Cursor cursor = dbHelper.getReviewsForEquipment(equipmentId);

            ArrayList<String> reviewList = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    int rating = cursor.getInt(0);
                    String text = cursor.getString(1);

                    // Combine rating and text into one string
                    String review = "Rating: " + rating + "★\n" + text;
                    reviewList.add(review);
                } while (cursor.moveToNext());
            }
            cursor.close();

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    reviewList
            );

            lv.setAdapter(adapter);
        }
    }
}
