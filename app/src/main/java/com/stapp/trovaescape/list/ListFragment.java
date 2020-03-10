package com.stapp.trovaescape.list;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stapp.trovaescape.R;
import com.stapp.trovaescape.data.EscapeData;
import com.stapp.trovaescape.details.EscapeDetails;
import com.stapp.trovaescape.main.MainActivity;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private static final String ESCAPE_DETAILS_FRAGMENT = "ESCAPE_DETAILS_FRAGMENT";

    private EditText searchEdit;
    private ArrayList<EscapeData> escapeList;
    private EscapeListAdapter adapter;
    private BottomNavigationView bottomNavigationView;

    private ArrayList<String> escapeNames = new ArrayList<>();

    public ListFragment(){//BottomNavigationView bottomNavigationView){
        //this.bottomNavigationView = bottomNavigationView;

        escapeNames.add("Nox-Escape Room!Via Aaaa 12!Horror!0");
        escapeNames.add("Locked Escape Room!Via Aaaa 12!Giallo!1");
        escapeNames.add("Escape Room Campo dei Fiori!Via Aaaa 12!Tag1!0");
        escapeNames.add("Game Over Escape Rooms!Via Aaaa 12!Tag2!0");
        escapeNames.add("La Casa degli Enigmi!Via Aaaa 12!Tag3!0");
        escapeNames.add("Escape Room Resolute!Via Aaaa 12!Tag4!0");
        escapeNames.add("Escape Room Adventure Rooms!Via Aaaa 12!Tag5!0");
        escapeNames.add("Questhouse!Via Aaaa 12!Tag6!0");
        escapeNames.add("Escape Room Roma 2!Via Aaaa 12!Tag7!1");
        escapeNames.add("Magic Escape Room!Via Aaaa 12!Tag8!0");
        escapeNames.add("Escape Room Roma!Via Aaaa 12!Tag9!0");
    }

    /*public static ListFragment newInstance(){//BottomNavigationView bottomNavigationView){
        return new ListFragment();//bottomNavigationView);
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.escape_list, container, false);
        searchEdit = v.findViewById(R.id.search_edit);
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

        final RecyclerView recEscape = v.findViewById(R.id.escapes_recycle);
        recEscape.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
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
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getListAtStart();
    }

    public void setBottomNavigationView(BottomNavigationView bottomNavigationView){
        this.bottomNavigationView = bottomNavigationView;
    }

    private void getListAtStart(){
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
    }

    private void getFilteredList(CharSequence txt){
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
    }

    private void openDetails(int pos){
        try {
            FragmentManager manager = getActivity().getSupportFragmentManager();//getChildFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_frame_layout, new EscapeDetails(), ESCAPE_DETAILS_FRAGMENT);
            transaction.addToBackStack(null);
            transaction.commit();
            bottomNavigationView.setVisibility(View.GONE);
        }catch (NullPointerException npe){
            Log.d("LIST_FRAGMENT_OPEN_DET", "Escape details fragment error!");
            Toast.makeText(getActivity(), R.string.det_frag_err, Toast.LENGTH_LONG).show();
        }
    }

}
