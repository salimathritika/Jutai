package com.example.jutai;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RentalDetailsActivity extends AppCompatActivity {
    Button submit;
    DatePicker dp;
    EditText e1;

    private int equipmentId;
    private String equipmentName;
    private double pricePerHour;
    private int renterId;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rental_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        submit=findViewById(R.id.buttonSubmitRental);
        e1=findViewById(R.id.editTextHours);
        dp=findViewById(R.id.datePicker);

        db=new DatabaseHelper(this);

        // Get equipment ID from Intent
        Intent intent = getIntent();
        equipmentId = intent.getIntExtra("equipmentId", -1);
        equipmentName = intent.getStringExtra("equipmentName");
        pricePerHour = intent.getDoubleExtra("pricePerHour", 0.0);

        // Get renter ID from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        renterId = prefs.getInt("UserId", -1);
        String renterName=db.getUserName(renterId);

        //for notifications
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(ContextCompat.checkSelfPermission(RentalDetailsActivity.this, android.Manifest.permission.POST_NOTIFICATIONS)!=
                    PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(RentalDetailsActivity.this,new String[]{android.Manifest.permission.POST_NOTIFICATIONS},101);
            }
        }




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int day = dp.getDayOfMonth();
                int month = dp.getMonth(); // 0-based
                int year = dp.getYear();
// Format as string (e.g., 2025-04-22)
                String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, day);

                String hoursStr = e1.getText().toString().trim();
                if (hoursStr.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter hours", Toast.LENGTH_SHORT).show();
                    return;
                }
                int sHours = Integer.parseInt(hoursStr);

                long rentalId = db.insertRental(renterId, equipmentId, selectedDate, sHours, pricePerHour * sHours);
                if (rentalId != -1) {
                    SharedPreferences sharedPref = getSharedPreferences("MyRental",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putLong("rentalId",rentalId);
                    editor.putInt("rentalHours",sHours);
                    editor.apply();
                    Toast.makeText(getApplicationContext(), "Rental inserted", Toast.LENGTH_SHORT).show();
                    makeNotifications((int) rentalId, renterName, equipmentName, sHours, selectedDate);

                }



            }
        });
    }

    public void makeNotifications(int rentalId, String renterName, String equipmentName, int sHours, String selectedDate) {

        String channelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setSmallIcon(R.drawable.baseline_person_24) // Make sure this exists!
                .setContentTitle("Rental added!")
                .setContentText(renterName+" wants to rent your "+ equipmentName+" for "+String.valueOf(sHours)+" hours on "+ selectedDate)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Intent for tapping the notification
        Intent intent = new Intent(getApplicationContext(), NotificationActionReceiver.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("data", "Equipment added");

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        // ===== ADD APPROVE / DECLINE ACTIONS =====
        Intent approveIntent = new Intent(getApplicationContext(), NotificationActionReceiver.class);
        approveIntent.setAction("APPROVE_ACTION");
        approveIntent.putExtra("rentalId", rentalId);
        PendingIntent approvePendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 1, approveIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent declineIntent = new Intent(getApplicationContext(), NotificationActionReceiver.class);
        declineIntent.setAction("DECLINE_ACTION");
        declineIntent.putExtra("rentalId", rentalId);
        PendingIntent declinePendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 2, declineIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);





        //do ic_check and ic_cross
        builder.addAction(R.drawable.baseline_person_24, "Approve", approvePendingIntent);
        builder.addAction(R.drawable.baseline_person_24, "Decline", declinePendingIntent);

        // Channel creation
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelID);
            if (notificationChannel == null) {
                String channelName = "Equipment Notifications";
                String channelDesc = "Notifications about equipment uploads";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(channelID, channelName, importance);
                notificationChannel.setDescription(channelDesc);
                notificationChannel.enableVibration(true);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.GREEN);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0, builder.build());

        int ownerId = db.getOwnerIdForEquipment(equipmentId); // your method to get owner


        if (db.insertNotification(ownerId,rentalId, renterName+" wants to rent your "+ equipmentName+" for "+String.valueOf(sHours)+" hours on "+ selectedDate,"rental_request"))
        {
            Toast.makeText(getApplicationContext(),"Notification added",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Notification not added",Toast.LENGTH_SHORT).show();
        }


    }
}