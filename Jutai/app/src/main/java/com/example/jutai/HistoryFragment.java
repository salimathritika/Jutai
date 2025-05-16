package com.example.jutai;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    ListView listView;
    ArrayList<String> rentals = new ArrayList<>();
    ArrayList<Integer> rentalIds = new ArrayList<>();
    ArrayAdapter<String> adapter;
    DatabaseHelper db;
    int userId = 0;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        listView = view.findViewById(R.id.displayRental);
        db = new DatabaseHelper(requireContext());

        SharedPreferences sharedPref = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        userId = sharedPref.getInt("UserId", 0);

        loadRentalPayments();

        listView.setOnItemClickListener((adapterView, view1, position, id) -> {
            int itemId = rentalIds.get(position);
            showReviewDialog(itemId, userId);
        });

        return view;
    }

    private void loadRentalPayments() {
        rentals = new ArrayList<>();
        rentalIds = new ArrayList<>();

        Cursor cursor = db.getRentalPaymentDetailsByUser(userId);
        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(getContext(), "No rentals found for user", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()) {
            String equipmentName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String rentedAt = cursor.getString(cursor.getColumnIndexOrThrow("created_at"));
            double amountPaid = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
            String paymentStatus = cursor.getString(cursor.getColumnIndexOrThrow("payment_status"));
            int equipmentId = cursor.getInt(4);

            rentalIds.add(equipmentId);
            rentals.add("Equipment: " + equipmentName +
                    "\nDate: " + rentedAt +
                    "\nAmount Paid: ₹" + amountPaid +
                    "\nStatus: " + paymentStatus);
        }

        cursor.close();

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, rentals);
        listView.setAdapter(adapter);
    }

    private void showReviewDialog(int equipmentId, int userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Leave a Review");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText ratingInput = new EditText(requireContext());
        ratingInput.setHint("Rating (1 to 5)");
        ratingInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(ratingInput);

        final EditText reviewInput = new EditText(requireContext());
        reviewInput.setHint("Write your review...");
        reviewInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        layout.addView(reviewInput);

        builder.setView(layout);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            String ratingStr = ratingInput.getText().toString().trim();
            String reviewText = reviewInput.getText().toString().trim();

            if (!ratingStr.isEmpty()) {
                int rating = Integer.parseInt(ratingStr);
                if (rating >= 1 && rating <= 5) {
                    boolean inserted = db.insertReview(equipmentId, userId, rating, reviewText);
                    if (inserted) {
                        Toast.makeText(getContext(), "Review submitted!", Toast.LENGTH_SHORT).show();
                        loadRentalPayments();
                    } else {
                        Toast.makeText(getContext(), "Failed to submit review.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Rating must be between 1 and 5.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Rating is required.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
