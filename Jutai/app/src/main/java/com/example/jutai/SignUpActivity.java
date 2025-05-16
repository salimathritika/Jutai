package com.example.jutai;

import android.content.Intent;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SignUpActivity extends AppCompatActivity {
    FloatingActionButton backbtn;
    EditText Name,Phone,Password,Email,Location;
    Button SignUpbtn;
    String phone_number,pw,name,email,location;

    private ActivityResultLauncher<Intent> locationLauncher; //to save the instance so that the entered values don't get lost when coming back

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        locationLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String loc = data.getStringExtra("location");
                            if (loc != null) {
                                Location.setText(loc);
                            }
                        }
                    }
                }
        );
        backbtn = findViewById(R.id.backbtn);
        Name = findViewById(R.id.Name);
        Phone = findViewById(R.id.Phone);
        Password = findViewById(R.id.Password);
        Email = findViewById(R.id.Email);
        Location = findViewById(R.id.Location);
        SignUpbtn= findViewById(R.id.SignUpbtn);
        DatabaseHelper db = new DatabaseHelper(this);

        //String loc = getIntent().getStringExtra("location");
        //Location.setText(loc);

        SignUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone_number= Phone.getText().toString().trim();
                pw = Password.getText().toString().trim();
                name = Name.getText().toString().trim();
                email = Email.getText().toString().trim();
                location = Location.getText().toString().trim();
                if(phone_number.isEmpty() || pw.isEmpty() || name.isEmpty() || location.isEmpty() || location.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Fill all Fields", Toast.LENGTH_SHORT).show();
                }
                if(phone_number.length() != 10)
                {
                    Toast.makeText(getApplicationContext(),"Invalid ! Phone number should be of 10 digits",Toast.LENGTH_SHORT).show();
                }
                if(db.userExists(phone_number))
                {
                    Toast.makeText(getApplicationContext(),"User already exists", Toast.LENGTH_SHORT).show();
                }
                boolean inserted = db.insertUser(name,phone_number,email,pw,location);
                if(inserted)
                {
                    Toast.makeText(getApplicationContext(),"Account Successfully Created",Toast.LENGTH_SHORT).show();
                    //Redirect to Login page
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Account Creation failed, Try Again",Toast.LENGTH_SHORT).show();
                }



            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });

        Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,CurrentLocation.class);
                locationLauncher.launch(intent);


            }
        });
    }
}