package com.example.jutai;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ViewNotifications extends AppCompatActivity {
        ListView notificationList;
        ArrayList<String> listItem;
    ArrayAdapter<String> adapter;
    ArrayList<Integer> notificationIds;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_notifications);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        db=new DatabaseHelper(this);
        notificationIds = new ArrayList<>();
        notificationList = findViewById(R.id.NotificationList);
        listItem = new ArrayList<>();

        viewData();

        notificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopupMenu popupMenu = new PopupMenu(ViewNotifications.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.notifications_menu, popupMenu.getMenu());

               int notifId = notificationIds.get(position);





                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();

                        Cursor rentalCursor = db.getRentalIdByNotifId(notifId);
                       // Cursor typeCursor = db.getTypeByNotifId(notifId);;
                        int rentalId = -1;
                        String notifType = "";

                        if (rentalCursor != null && rentalCursor.moveToFirst()) {
                            rentalId = rentalCursor.getInt(rentalCursor.getColumnIndexOrThrow("rental_id"));
                            notifType = rentalCursor.getString(rentalCursor.getColumnIndexOrThrow("notification_type"));
                            rentalCursor.close();
                        }

// 🚨 Check if it's a request notification
//                        if (!"rental_request".equalsIgnoreCase(notifType)) {
//                            Toast.makeText(getApplicationContext(), "This action is only allowed on request notifications.", Toast.LENGTH_SHORT).show();
//                            return false;
//                        }



                        if (itemId == R.id.approve) {
                            boolean updated = db.updateRentalStatus(rentalId, "confirmed");
                            if (updated) {
                                Toast.makeText(getApplicationContext(), "Rental approved", Toast.LENGTH_SHORT).show();

                                // Send notification to the renter
                                Cursor rentalDetailsCursor = db.getRentalDetails(rentalId);
                                if (rentalDetailsCursor != null && rentalDetailsCursor.moveToFirst()) {
                                    int renterId = rentalDetailsCursor.getInt(rentalDetailsCursor.getColumnIndexOrThrow("renter_id"));
                                    String equipmentName = rentalDetailsCursor.getString(rentalDetailsCursor.getColumnIndexOrThrow("equipment_name"));

                                    sendNotificationToRenter(getApplicationContext(), rentalId, renterId, equipmentName,"approved");
                                    db.insertNotification(renterId,rentalId, "Your request for " + equipmentName + " has been approved by the owner.", "rental_approved");
                                    // Send notification to renter
                                    rentalDetailsCursor.close();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to approve rental", Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        }else if (itemId == R.id.decline) {
                            boolean updated = db.updateRentalStatus(rentalId, "cancelled");
                            if (updated) {
                                Toast.makeText(getApplicationContext(), "Rental cancelled", Toast.LENGTH_SHORT).show();
                                // Send notification to the renter
                                Cursor rentalDetailsCursor = db.getRentalDetails(rentalId);
                                if (rentalDetailsCursor != null && rentalDetailsCursor.moveToFirst()) {
                                    int renterId = rentalDetailsCursor.getInt(rentalDetailsCursor.getColumnIndexOrThrow("renter_id"));
                                    String equipmentName = rentalDetailsCursor.getString(rentalDetailsCursor.getColumnIndexOrThrow("equipment_name"));

                                    sendNotificationToRenter(getApplicationContext(), rentalId, renterId, equipmentName,"declined");
                                    db.insertNotification(renterId, rentalId, "Your request for " + equipmentName + " has been declined by the owner.", "rental_declined");
                                    // Send notification to renter
                                    rentalDetailsCursor.close();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to reject rental", Toast.LENGTH_SHORT).show();
                            }
                        }

                        else if (itemId == R.id.pay) {
                            //boolean updated = db.updateRentalStatus(rentalId, "cancelled");

                            Intent intent= new Intent(ViewNotifications.this,PaymentActivity.class);
                            startActivity(intent);
//                            if (updated) {
//                                Toast.makeText(getApplicationContext(), "Rental cancelled", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(getApplicationContext(), "Failed to reject rental", Toast.LENGTH_SHORT).show();
//                            }
                        }

                        return false;
                    }
                });

                popupMenu.show();
            }
        });







    }
    private void viewData() {

        notificationIds.clear();
        listItem.clear();




        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs",MODE_PRIVATE);
        int userId = sharedPref.getInt("UserId",0);
        Cursor cursor = db.getNotificationsForUser(userId);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()) {

            listItem.add("Notification:" + cursor.getString(2));
            notificationIds.add(cursor.getInt(cursor.getColumnIndexOrThrow("notification_id")));

        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
        notificationList.setAdapter(adapter);
    }

    private void sendNotificationToRenter(Context context, int rentalId, int renterId, String equipmentName, String statusMessage) {
        SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        int currentUserId = prefs.getInt("UserId", -1);

        if (currentUserId != renterId) {
            Log.d("Notification", "User is not the renter. Skipping notification.");
            return; // skip if someone else is logged in
        }

        Log.d("NotificationReceiver", "Current User ID: " + currentUserId + " Renter ID: " + renterId);
        if (currentUserId != renterId) {
            Log.d("NotificationReceiver", "Notification skipped: Current user is not the renter.");
            return; // Skip if someone else is logged in
        }

//        // Create an Intent for PaymentActivity
//        Intent paymentIntent = new Intent(context, PaymentActivity.class);
//        paymentIntent.putExtra("rentalId", rentalId); // Pass rentalId to the PaymentActivity
//
//        // Create a PendingIntent that will be triggered when the notification is clicked
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, paymentIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        String channelID = "CHANNEL_ID_RENTER";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.baseline_person_24)
                .setContentTitle("Rental"+ statusMessage)
                .setContentText("Your request for " + equipmentName + " has been "+  statusMessage +" by the owner.")
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
        db.insertNotification(renterId,rentalId, "Your request for " + equipmentName + " has been "+  statusMessage +" by the owner.", "rental_"+statusMessage);

    }
}
