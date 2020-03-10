package com.stapp.trovaescape.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stapp.trovaescape.R;
import com.stapp.trovaescape.data.EscapeData;
import com.stapp.trovaescape.details.EscapeDetails;
import com.stapp.trovaescape.list.ListFragment;
import com.stapp.trovaescape.map.MapFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LIST_FRAGMENT = "LIST_FRAGMENT";
    private static final String MAP_FRAGMENT = "MAP_FRAGMENT";

    private BottomNavigationView bottomNavigation;

    final FragmentManager fragmentManager = getSupportFragmentManager();
    final Fragment listFragment = new ListFragment();
    final Fragment mapFragment = new MapFragment();
    private Fragment currentFragment = listFragment;

    //private EditText searchEdit;
    //private ArrayList<EscapeData> escapeList;
    //private EscapeListAdapter adapter;

    //private ArrayList<String> escapeNames = new ArrayList<>();

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

        //openFragment(ListFragment.newInstance(), LIST_FRAGMENT);//bottomNavigation));

        /*escapeNames.add("Nox-Escape Room!Via Aaaa 12!Horror!0");
        escapeNames.add("Locked Escape Room!Via Aaaa 12!Giallo!1");
        escapeNames.add("Escape Room Campo dei Fiori!Via Aaaa 12!Tag1!0");
        escapeNames.add("Game Over Escape Rooms!Via Aaaa 12!Tag2!0");
        escapeNames.add("La Casa degli Enigmi!Via Aaaa 12!Tag3!0");
        escapeNames.add("Escape Room Resolute!Via Aaaa 12!Tag4!0");
        escapeNames.add("Escape Room Adventure Rooms!Via Aaaa 12!Tag5!0");
        escapeNames.add("Questhouse!Via Aaaa 12!Tag6!0");
        escapeNames.add("Escape Room Roma 2!Via Aaaa 12!Tag7!1");
        escapeNames.add("Magic Escape Room!Via Aaaa 12!Tag8!0");
        escapeNames.add("Escape Room Roma!Via Aaaa 12!Tag9!0");*/

        /*searchEdit = findViewById(R.id.search_edit);
        searchEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0)
                    getFilteredList(s);
                else
                    getListAtStart();
            }
        });

        final RecyclerView recEscape = findViewById(R.id.escapes_recycle);
        recEscape.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recEscape.setLayoutManager(layoutManager);
        escapeList = new ArrayList<>();
        adapter = new EscapeListAdapter(escapeList);
        recEscape.setAdapter(adapter);
        adapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetails(recEscape.getChildAdapterPosition(v));
            }
        });
        getListAtStart();

        FloatingActionButton fab_all = findViewById(R.id.fab_map);
        fab_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEscapeMap();
            }
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentManager.beginTransaction().show(currentFragment).commit();
        if(bottomNavigation.getVisibility() == View.GONE && !EscapeDetails.isOpen){
            bottomNavigation.setVisibility(View.VISIBLE);
        }
        //openFragment(ListFragment.newInstance(), LIST_FRAGMENT);
        //getListAtStart();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(bottomNavigation.getVisibility() == View.GONE && !EscapeDetails.isOpen){
            bottomNavigation.setVisibility(View.VISIBLE);
        }
    }

    /*public void openFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_layout, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }*/

    /*private void getListAtStart(){
        escapeList.clear();
        for(int i = 0; i < escapeNames.size(); i++){
            String[] s = escapeNames.get(i).split("!");

            EscapeData e = new EscapeData();
            e.setName(s[0]);
            e.setAddress(s[1]);
            e.setTags(s[2]);
            String x = s[3];
            if(x.equals("0"))
                e.setFree(false);
            else if(x.equals("1"))
                e.setFree(true);
            escapeList.add(e);
        }
        adapter.notifyDataSetChanged();
    }*/

    /*private void getFilteredList(CharSequence txt){
        escapeList.clear();
        for(int i = 0; i < escapeNames.size(); i++){
            String[] s = escapeNames.get(i).split("!");

            if(s[0].toLowerCase().contains(txt.toString().toLowerCase())) {
                EscapeData e = new EscapeData();
                e.setName(s[0]);
                e.setAddress(s[1]);
                e.setTags(s[2]);
                String x = s[3];
                if (x.equals("0"))
                    e.setFree(false);
                else if (x.equals("1"))
                    e.setFree(true);
                escapeList.add(e);
            }
        }
        adapter.notifyDataSetChanged();
    }*/

    /*private void openDetails(int pos){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_layout, new EscapeDetails(), ESCAPE_DETAILS_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
    }*/

    /*private void getEscapeList(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.main_layout, new ListFragment(), LIST_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
    }*/

    /*private void getEscapeMap(){
        //Intent intent = new Intent(this, MapFragment.class);
        //startActivity(intent);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.main_layout, new MapFragment(), MAP_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
    }*/
}
