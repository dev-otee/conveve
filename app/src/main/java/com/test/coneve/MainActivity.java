package com.test.coneve;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private FragmentContainerView fragmentContainerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initially set to fetch event fragment
        EventFragment eventFragment = new EventFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, eventFragment)
                .addToBackStack(null)
                .commit();

        ((BottomNavigationView) findViewById(R.id.main_activity_bottom_navigation)).setOnItemSelectedListener(new BottomNavBarEventHandler());

    }

    class BottomNavBarEventHandler implements NavigationBarView.OnItemSelectedListener{

        int currentItem;

        public BottomNavBarEventHandler() {
            this.currentItem = R.id.events;
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(currentItem == item.getItemId())
                return true;

            switch(item.getItemId()){
                case R.id.events:
                    currentItem = R.id.events;
                    // load event fragment in fragment container
                    EventFragment eventFragment = new EventFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container_view, eventFragment)
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.profile:
                    currentItem = R.id.profile;
                    ProfileFragment profileFragment = new ProfileFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container_view, profileFragment)
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.qr_scanner:
                    currentItem = R.id.qr_scanner;
                    QrScannerFragment qrScannerFragment = new QrScannerFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container_view, qrScannerFragment)
                            .addToBackStack(null)
                            .commit();
                    break;
            };
            return true;
        }
    }
}