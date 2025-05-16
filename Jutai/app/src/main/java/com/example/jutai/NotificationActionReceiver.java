package com.example.jutai;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;

public class NotificationActionReceiver extends BroadcastReceiver {

    DatabaseHelper db;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int rentalId = intent.getIntExtra("rentalId", -1);
        db = new DatabaseHelper(context);

        if ("APPROVE_ACTION".equals(action)) {
            // Update the rental status to "confirmed" after the owner approves the request
            db.updateRentalStatus(rentalId, "confirmed");
            Toast.makeText(context, "Approved ✅", Toast.LENGTH_SHORT).show();

            // Get rental details (renterId and equipment name)
            Cursor cursor = db.getRentalDetails(rentalId);
            if (cursor.moveToFirst()) {
                int renterId = cursor.getInt(0); // renterId
                String equipmentName = cursor.getString(1); // equipment name
                cursor.close();

                // Now notify the renter that the request has been approved
                sendNotificationToRenter(context,rentalId, renterId, equipmentName,"approved"); // Send notification to renter
            }
        }

        else if ("DECLINE_ACTION".equals(action)) {
            db.updateRentalStatus(rentalId, "cancelled");
            Toast.makeText(context, "Declined ❌", Toast.LENGTH_SHORT).show();

            Cursor cursor = db.getRentalDetails(rentalId);
            if (cursor.moveToFirst()) {
                int renterId = cursor.getInt(0); // renterId
                String equipmentName = cursor.getString(1); // equipment name
                cursor.close();

                // Now notify the renter that the request has been approved
                sendNotificationToRenter(context,rentalId, renterId, equipmentName,"declined"); // Send notification to renter
            }
        }

        String status = db.getRentalStatus(rentalId);
        Log.d("StatusCheck", "Rental " + rentalId + " current status: " + status);

    }

    private void sendNotificationToRenter(Context context,int rentalId, int renterId, String equipmentName, String statusMessage) {
//        SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
//        int currentUserId = prefs.getInt("UserId", -1);
//
//        if (currentUserId != renterId) {
//            Log.d("Notification", "User is not the renter. Skipping notification.");
//            return; // skip if someone else is logged in
//        }
//
//        Log.d("NotificationReceiver", "Current User ID: " + currentUserId + " Renter ID: " + renterId);
//        if (currentUserId != renterId) {
//            Log.d("NotificationReceiver", "Notification skipped: Current user is not the renter.");
//            return; // Skip if someone else is logged in
//        }


        String channelID = "CHANNEL_ID_RENTER";



        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.baseline_person_24)
                .setContentTitle("Rental "+statusMessage)
                .setContentText("Your request for " + equipmentName + " has been "+statusMessage+" by the owner.")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelID,
                    "Renter Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications to renter after approval");
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        db.insertNotification(renterId,rentalId, "Your request for " + equipmentName + " has been "+statusMessage+" by the owner.", "rental_"+statusMessage);

    }

}