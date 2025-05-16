package com.example.jutai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    Button SignInbtn,VerifyOtpbtn;
    String phone_number;
    String pw;
    EditText Phone,Password,OtpInput;

    FirebaseAuth mAuth;
    FloatingActionButton backbtn;
    DatabaseHelper db;
    String verificationId;
    TextView SignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        backbtn = findViewById(R.id.backbtn);
        SignInbtn=findViewById(R.id.SignInbtn);
        Phone=findViewById(R.id.Phone);
        Password=findViewById(R.id.Password);
        mAuth = FirebaseAuth.getInstance();
        db = new DatabaseHelper(this);
        SignUp = findViewById(R.id.SignUp);
        VerifyOtpbtn = findViewById(R.id.VerifyOtpbtn);
        OtpInput = findViewById(R.id.OtpInput);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this,MainActivity2.class);
                startActivity(intent);

            }
        });
        SignInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone_number = Phone.getText().toString().trim();
                pw = Password.getText().toString().trim();
                if (phone_number.isEmpty() || pw.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                }

                else if (db.verifyUser(phone_number, pw)) {
                    int UserId = db.getUserid(phone_number);
                    Toast.makeText(getApplicationContext(),"id is:"+ UserId,Toast.LENGTH_SHORT).show();
                    phone_number ="+91" + Phone.getText().toString().trim();


                    SharedPreferences sharedpref = getSharedPreferences("MyAppPrefs",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpref.edit();
                    editor.putInt("UserId",UserId);
                    editor.putBoolean("IsLoggedIn",true);
                    editor.apply();
                    startPhoneVerification(phone_number); // Add your country code



                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
        VerifyOtpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = OtpInput.getText().toString();
                if (verificationId != null) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
    private void startPhoneVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        //when device automatically verifies with the otp
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        //when verification fails
                        Toast.makeText(LoginActivity.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(String verId, PhoneAuthProvider.ForceResendingToken token) {
                        //when otp is sent to the phone
                        verificationId = verId;
                        Toast.makeText(LoginActivity.this, "OTP sent to your phone", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {




                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,MainPageActivity.class);
                startActivity(intent);
                // Redirect to main activity
            } else {
                Toast.makeText(this, "OTP verification failed", Toast.LENGTH_SHORT).show();
                //credential stores the otp entered by the user
            }
        });
    }
}