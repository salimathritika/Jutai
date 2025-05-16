package com.example.jutai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {
    Button loginbtn;
    Button registerbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load the selected language
        LocaleHelper.loadLocale(this);



        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loginbtn = findViewById(R.id.loginbtn);
        registerbtn = findViewById(R.id.registerbtn);
        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs",MODE_PRIVATE);
        boolean flag = sharedPref.getBoolean("IsLoggedIn",false);
        if(flag)
        {
            Intent intent = new Intent(MainActivity2.this, MainPageActivity.class);
            startActivity(intent);
        }

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent loginIntent = new Intent(MainActivity2.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences preferences = newBase.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String language = preferences.getString("Locale.Language", "en"); // Default to English
        Context context = LocaleHelper.updateResources(newBase, language);
        super.attachBaseContext(context); // ✅ Use the returned context
    }





}
