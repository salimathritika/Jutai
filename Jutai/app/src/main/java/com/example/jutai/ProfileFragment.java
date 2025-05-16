package com.example.jutai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ProfileFragment extends Fragment {

    ListView lv;
    TextView profileText;
    ArrayList<String> options = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        lv = view.findViewById(R.id.lv);
        profileText = view.findViewById(R.id.profileText);
        options.add("Notifications");
        options.add("My Bookings");
        options.add("My Listings");
        options.add("Profile Settings");
        options.add("Logout");

        SharedPreferences sharedPref = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);

        int userId = sharedPref.getInt("UserId",0);
        DatabaseHelper db = new DatabaseHelper(requireContext());
        String username = db.getUserName(userId);
        profileText.setText(username+"'s" + " Profile");

        ArrayAdapter adapter = new ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,options);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Option = options.get(position);

                if(Option.equalsIgnoreCase("Logout"))
                {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("IsLoggedIn",false);
                    editor.apply();
                    Intent intent = new Intent(requireContext(), MainActivity2.class);
                    startActivity(intent);
                }
                else if (Option.equalsIgnoreCase("Profile Settings")) {
                    PopupMenu popupMenu = new PopupMenu(requireContext(), view);
                    popupMenu.getMenuInflater().inflate(R.menu.profile_settings_menu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int itemId = item.getItemId();

                            if (itemId == R.id.edit_profile) {
                               Intent intent = new Intent(requireContext(), EditProfileActivity.class);
                               startActivity(intent);
                                // Add your intent or fragment logic here
                                return true;
                            } else if (itemId == R.id.delete_profile) {
                                if(db.deleteUser(userId))
                                {
                                    Toast.makeText(requireContext(), "Profile Deleted", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(requireContext(), MainActivity.class);
                                    startActivity(intent);
                                }

                                // Show confirmation dialog before deletion
                                return true;
                            }

                            return false;
                        }
                    });

                    popupMenu.show();
                }
                else if(Option.equalsIgnoreCase("My Listings"))
                {
                    Intent intent = new Intent(requireContext(),MyListings.class);
                    startActivity(intent);
                }
                else if(Option.equalsIgnoreCase("Notifications"))
                {
                    Intent intent = new Intent(requireContext(),ViewNotifications.class);
                    startActivity(intent);
                }
            }
        });

        return view;

    }
}