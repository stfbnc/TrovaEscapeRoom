package com.stapp.trovaescape.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stapp.trovaescape.R;
import com.stapp.trovaescape.data.Escape;
import com.stapp.trovaescape.data.Room;
import com.stapp.trovaescape.details.EscapeDetails;
import com.stapp.trovaescape.list.ListFragment;
import com.stapp.trovaescape.map.MapFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LIST_FRAGMENT = "LIST_FRAGMENT";
    private static final String MAP_FRAGMENT = "MAP_FRAGMENT";

    public static String filter = ""; // same filter for both fragments

    private BottomNavigationView bottomNavigation;

    final FragmentManager fragmentManager = getSupportFragmentManager();
    final Fragment listFragment = new ListFragment();
    final Fragment mapFragment = new MapFragment();
    private Fragment currentFragment = listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager.beginTransaction().add(R.id.main_layout, mapFragment, MAP_FRAGMENT).hide(mapFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_layout, listFragment, LIST_FRAGMENT).commit();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_list:
                        //openFragment(ListFragment.newInstance(), LIST_FRAGMENT);//bottomNavigation));
                        fragmentManager.beginTransaction().hide(currentFragment).show(listFragment).commit();
                        currentFragment = listFragment;
                        return true;
                    case R.id.navigation_map:
                        fragmentManager.beginTransaction().hide(currentFragment).show(mapFragment).commit();
                        currentFragment = mapFragment;
                        //openFragment(MapFragment.newInstance(), MAP_FRAGMENT);
                        return true;
                }
                return false;
            }
        });

        ((ListFragment) listFragment).setBottomNavigationView(bottomNavigation);

    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentManager.beginTransaction().show(currentFragment).commit();
        if(bottomNavigation.getVisibility() == View.GONE && !EscapeDetails.isOpen){
            bottomNavigation.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(bottomNavigation.getVisibility() == View.GONE && !EscapeDetails.isOpen){
            bottomNavigation.setVisibility(View.VISIBLE);
        }
    }

}
