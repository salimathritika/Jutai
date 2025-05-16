package com.example.jutai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button getstartedbtn;
    Spinner spinner;
    ArrayList<String> languages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load saved language before setting the content view
        LocaleHelper.loadLocale(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getstartedbtn = findViewById(R.id.getstartedbtn);
        spinner = findViewById(R.id.spinner);

        // Add languages to the spinner
        languages.add("Select Language");
        languages.add("English");
        languages.add("Hindi");

        // Set up the spinner adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Get the saved language and set the correct spinner position
        SharedPreferences preferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String savedLanguage = preferences.getString("Locale.Language", "en");
        if (savedLanguage.equals("en")) {
            spinner.setSelection(1); // English
        } else if (savedLanguage.equals("hi")) {
            spinner.setSelection(2); // Hindi
        } else {
            spinner.setSelection(0); // Default
        }

        // Handle language selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return; // Skip "Select Language"
                }

                // Get selected language and map it to language codes
                String selectedLanguage = languages.get(position);
                String languageCode = "en"; // Default to English

                if (selectedLanguage.equals("English")) {
                    languageCode = "en";
                } else if (selectedLanguage.equals("Hindi")) {
                    languageCode = "hi";
                }

                // Get the currently selected language from SharedPreferences
                String currentLanguage = preferences.getString("Locale.Language", "en");

                // Only change language and restart if the selected language is different
                if (!languageCode.equals(currentLanguage)) {
                    LocaleHelper.setLocale(MainActivity.this, languageCode);

                    // Restart activity to apply changes
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Handle button click to navigate to the next activity
        getstartedbtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(intent);
        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences preferences = newBase.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String language = preferences.getString("Locale.Language", "en"); // Default to English
        super.attachBaseContext(LocaleHelper.updateResources(newBase, language));
    }

}
