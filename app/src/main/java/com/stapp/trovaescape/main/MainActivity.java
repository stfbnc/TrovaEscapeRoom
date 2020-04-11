package com.stapp.trovaescape.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stapp.trovaescape.R;
import com.stapp.trovaescape.data.Constants;
import com.stapp.trovaescape.data.Escape;
import com.stapp.trovaescape.data.Room;
import com.stapp.trovaescape.data_management.DataRetriever;
import com.stapp.trovaescape.data_management.VolleyResponseListener;
import com.stapp.trovaescape.db.DataManager;
import com.stapp.trovaescape.details.EscapeDetails;
import com.stapp.trovaescape.list.ListFragment;
import com.stapp.trovaescape.map.MapFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

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

        final DataRetriever dataRetriever = new DataRetriever(this);
        final DataManager dataManager = new DataManager(this);
        final Handler handler = new Handler();
        handler.postDelayed( new Runnable() {

            @Override
            public void run() {
                Log.d("AAAAAA", "BBBBBB");
                checkForData(dataRetriever, dataManager);
                handler.postDelayed( this, Constants.DELAY);
            }

        }, Constants.DELAY);

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
        ((MapFragment) mapFragment).setBottomNavigationView(bottomNavigation);

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

    public void checkForData(final DataRetriever dr, final DataManager dm){
        final RequestQueue rs = Volley.newRequestQueue(this);
        // get time
        dr.sendJsonRequest(rs, Constants.LTU_URL, new VolleyResponseListener() {
            @Override
            public void onErrorResponse(String message) {
                Log.d("respErrorLTU", "responseErrorLTU = " + message);
            }

            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    Log.d("respOKLTU", "responseOKLTU = " + response);
                    String responseStr = response.toString().trim();
                    Pattern regex = Pattern.compile("\"[0-9]{14}\"");
                    if (regex.matcher(responseStr).find()) {
                        final String ltu = dr.getTimeString(response);
                        String dbTime = dm.getDbTime();

                        // get data
                        if(!ltu.equals(Constants.NULL_TIME)){
                            if(Long.valueOf(ltu) > Long.valueOf(dbTime)) {
                                dr.sendJsonRequest(rs, Constants.DATA_URL, new VolleyResponseListener() {
                                    @Override
                                    public void onErrorResponse(String message) {
                                        Log.d("respErrorDATA", "responseErrorDATA = " + message);
                                    }

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        if (response != null) {
                                            Log.d("respOKDATA", "responseOKDATA = " + response);
                                            ArrayList<Escape> escapes = dr.getEscapesArray(response);
                                            if (escapes.size() > 0) {
                                                dm.updateDbTime(ltu);
                                                dm.fillDb(escapes);
                                                ((ListFragment) listFragment).getEscapeList(filter);
                                                MapFragment mf = ((MapFragment) mapFragment);
                                                if(mf.mMap != null)
                                                    mf.onMapReady(mf.mMap);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }else{
                        Log.d("wrongDateLTU", "responseLTU = " + response);
                    }
                } else {
                    Log.d("respNullLTU", "responseLTU = null");
                }
            }
        });
    }

}
