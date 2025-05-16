package com.example.jutai;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    ViewPager2 viewPager2;
    TextView welcomeText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout FIRST
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // THEN get the ViewPager2
        viewPager2 = view.findViewById(R.id.recentlyViewedViewPager);
        welcomeText = view.findViewById(R.id.welcomeText);
        DatabaseHelper db = new DatabaseHelper(requireContext());

        SharedPreferences sharedPref = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("UserId", 0);

        String username = db.getUserName(userId);
        if (username.equalsIgnoreCase("null")) {
            welcomeText.setText("Welcome guest!");
        } else {
            welcomeText.setText("Welcome " + username + "!");
        }

        //Get the list of images (byte arrays)
        List<byte[]> imageList = db.getRecentlyViewedImagesForUser(userId);

        // Convert byte[] to Bitmap list
        List<Bitmap> bitmapList = new ArrayList<>();
        for (byte[] imageBytes : imageList) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            bitmapList.add(bitmap);
        }

        // Set up the adapter
        RecentlyViewedAdapter adapter = new RecentlyViewedAdapter(requireContext(), bitmapList);
        viewPager2.setAdapter(adapter);

        // Auto-scroll handler (optional)
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int totalItems = adapter.getItemCount();
                if (totalItems > 0) {
                    int currentItem = viewPager2.getCurrentItem();
                    viewPager2.setCurrentItem((currentItem + 1) % totalItems);
                    handler.postDelayed(this, 3000); // every 3 sec
                } else {
                    // Optionally retry after some time
                    handler.postDelayed(this, 3000);
                }
            }
        };
        handler.postDelayed(runnable, 3000);


        return view;

    }
}
