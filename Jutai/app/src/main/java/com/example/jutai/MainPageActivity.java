package com.example.jutai;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

import com.example.jutai.HistoryFragment;
import com.example.jutai.HomeFragment;
import com.example.jutai.ProfileFragment;
import com.example.jutai.R;
import com.example.jutai.ViewFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainPageActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    FloatingActionButton addNew,faqFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        frameLayout = findViewById(R.id.frameLayout);
        addNew = findViewById(R.id.addNew);
        faqFab = findViewById(R.id.faqFab);
        addNew.bringToFront();
        faqFab.bringToFront();
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
        loadFragment(new HomeFragment(),true);//when app loads , this will be the first fragment to load
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainPageActivity.this,AddNewActivity.class);
                startActivity(intent);


            }
        });

        faqFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, FAQActivity.class);
                startActivity(intent);

            }
        });
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