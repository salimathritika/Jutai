package com.example.jutai;

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

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity {
    private TextView amountEdt;
    private Button payBtn;
    private EditText hours;
    int userId=0, equipmentId=0;
    double amount=0.0;
    int no_of_hours=0;

    String username,phoneNumber;
    DatabaseHelper db;
    long rentalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        amountEdt = findViewById(R.id.idEdtAmount);
        payBtn = findViewById(R.id.idBtnPay);
        hours = findViewById(R.id.hours);
        db = new DatabaseHelper(this);


        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = sharedPref.getInt("UserId", 0);
        username = db.getUserName(userId).toString();
        phoneNumber = db.getUserPhoneNumber(userId).toString();
        SharedPreferences sharedPreferences = getSharedPreferences("MyRental",MODE_PRIVATE);
        rentalId = sharedPreferences.getLong("rentalId",0);
        no_of_hours = sharedPreferences.getInt("rentalHours",0);





        equipmentId = db.getEquipmentIdByRentalId((int)rentalId);
        hours.setText(String.valueOf(no_of_hours));

        try {
            no_of_hours = Integer.parseInt(hours.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Please enter a valid number of hours", Toast.LENGTH_SHORT).show();
            return;  // Return early to prevent further processing
        }

        if (no_of_hours <= 0) {
            Toast.makeText(getApplicationContext(), "Hours should be at least 1", Toast.LENGTH_SHORT).show();
            return;
        }
        double price = db.getEquipmentPrice(equipmentId);
        if (price == -1) {
            Toast.makeText(getApplicationContext(), "Could not fetch price", Toast.LENGTH_SHORT).show();
            return;
        } else {
            amountEdt.setText(String.valueOf(price * no_of_hours));
        }

        String amountStr = amountEdt.getText().toString().trim();

        // Check if the amount is empty
        if (amountStr.isEmpty()) {
            amountEdt.setError("Amount cannot be empty");
            return;
        }

         amount = 0;
        try {
            amount = Float.parseFloat(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Invalid amount entered", Toast.LENGTH_SHORT).show();
            return;
        }




        payBtn.setOnClickListener(v -> {





            // Initialize Razorpay checkout
            Checkout checkout = new Checkout();
            checkout.setKeyID("razorpay_key_here"); //Please fill you razorpay key detail here
            checkout.setImage(R.drawable.img);

            // Prepare payment object
            JSONObject object = new JSONObject();
            try {
                object.put("name", username);
                object.put("description", "Test payment");
                object.put("theme.color", "#F57224"); // Example color code
                object.put("currency", "INR");
                object.put("amount", amount * 100);  // Razorpay expects the amount in paise (cents)
                object.put("prefill.contact", phoneNumber);
                object.put("prefill.email", "test@gmail.com");

                // Start Razorpay Checkout
                checkout.open(PaymentActivity.this, object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment is successful : " + s, Toast.LENGTH_SHORT).show();
        boolean res=db.insertPayment((int)rentalId,userId,equipmentId,amount,"success");
        if(res)
            Toast.makeText(getApplicationContext(),"Insertion done",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),"Insertion failed",Toast.LENGTH_SHORT).show();
    }
    public void onPaymentError(int code, String response) {
        Log.e("Razorpay Error", "Error code: " + code + " Response: " + response);
        Toast.makeText(this, "Payment Failed due to error: " + response, Toast.LENGTH_SHORT).show();
        Boolean res=db.insertPayment((int)rentalId,userId,equipmentId,amount,"fail");
        if(res)
            Toast.makeText(getApplicationContext(),"Insertion done",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),"Insertion failed",Toast.LENGTH_SHORT).show();

    }
}