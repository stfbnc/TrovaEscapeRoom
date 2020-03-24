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

    public static ArrayList<Escape> e = new ArrayList<>();
    {Escape a = new Escape();
    a.setName("Magic Escape");
    a.setAddress("Via Inventata 24");
    a.setPhone("06777777777");
    a.setWebsite("https:://magic.escape/sito/");
    a.setCoords(new LatLng(41.935091, 12.422353));
    ArrayList<Room> r = new ArrayList<>();
    Room ra = new Room();
    ra.setName("Vendetta");
    ra.setWebsite("https:://magic.escape/sito/vendetta");
    ArrayList<String> p = new ArrayList<>();
    p.add("20€");
    p.add("25€");
    ra.setPrices(p);
    ArrayList<String> av = new ArrayList<>();
    av.add("19:30");
    av.add("20:50");
    ra.setAvailabilities(av);
    ArrayList<String> t = new ArrayList<>();
    t.add("bello");
    t.add("horror");
    ra.setTags(t);
    r.add(ra);
    a.setRooms(r);

    Escape b = new Escape();
    b.setName("Fugacemente");
    b.setAddress("Via Bella 76");
    b.setPhone("0645454545");
    b.setWebsite("https:://fugacemente/sito/");
    b.setCoords(new LatLng(41.891307, 12.531987));
    ArrayList<Room> rr = new ArrayList<>();
    Room rb = new Room();
    rb.setName("Camera");
    rb.setWebsite("https:://fugacemente/sito/camera");
    ArrayList<String> pb = new ArrayList<>();
    pb.add("20€");
    pb.add("25€");
    rb.setPrices(pb);
    ArrayList<String> avb = new ArrayList<>();
    avb.add("19:30");
    avb.add("20:50");
    rb.setAvailabilities(avb);
    ArrayList<String> tb = new ArrayList<>();
    tb.add("brutto");
    tb.add("giallo");
    rb.setTags(tb);
    rr.add(rb);
    b.setRooms(rr);

    e.add(a);
    e.add(b);}

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
