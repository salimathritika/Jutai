package com.example.jutai;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        frameLayout = findViewById(R.id.frameLayout);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.navViewSection)
                {
                    loadFragment(new ViewFragment(),false);


                }
                else if(itemId == R.id.navUserProfile)
                {
                    loadFragment(new ProfileFragment(),false);
                }
                else if(itemId == R.id.navRecent)
                {
                   loadFragment(new HistoryFragment(),false);

                }
                else
                {
                    loadFragment(new HomeFragment(),false);
                }



                return true;
            }
        });
        loadFragment(new HomeFragment(),true); //when app loads , this will be the first fragment to load
    }
    private void loadFragment(Fragment fragment, boolean isAppInitialised) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (isAppInitialised) {
            transaction.add(R.id.frameLayout, fragment);

        }
        else {
            transaction.replace(R.id.frameLayout, fragment);
        }
            transaction.commit();

    }
}