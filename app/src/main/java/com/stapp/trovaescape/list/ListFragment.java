package com.stapp.trovaescape.list;

import android.content.Context;
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
import com.stapp.trovaescape.data.Escape;
import com.stapp.trovaescape.db.DataManager;
import com.stapp.trovaescape.details.EscapeDetails;
import com.stapp.trovaescape.main.MainActivity;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private static final String ESCAPE_DETAILS_FRAGMENT = "ESCAPE_DETAILS_FRAGMENT";

    private EditText searchEdit;
    private ArrayList<Escape> escapeList = new ArrayList<>();
    //private String filter = "";
    private EscapeListAdapter adapter;
    private BottomNavigationView bottomNavigationView;

    public ListFragment(){//BottomNavigationView bottomNavigationView){
        //this.bottomNavigationView = bottomNavigationView;
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
                    MainActivity.filter = s.toString();
                else
                    MainActivity.filter = "";
                getEscapeList(MainActivity.filter);
            }
        });

        final RecyclerView recEscape = v.findViewById(R.id.escapes_recycle);
        recEscape.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recEscape.setLayoutManager(layoutManager);
        adapter = new EscapeListAdapter(escapeList);
        recEscape.setAdapter(adapter);
        adapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetails(recEscape.getChildAdapterPosition(v));
            }
        });
        getEscapeList(MainActivity.filter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getEscapeList(MainActivity.filter);
        searchEdit.setText(MainActivity.filter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        getEscapeList(MainActivity.filter);
        searchEdit.setText(MainActivity.filter);
    }

    public void setBottomNavigationView(BottomNavigationView bottomNavigationView){
        this.bottomNavigationView = bottomNavigationView;
    }

    public void getEscapeList(String seq){
        escapeList.clear();
        DataManager dm = new DataManager(getContext());
        escapeList.addAll(dm.getEscapes(seq));
        adapter.notifyDataSetChanged();
    }

    private void openDetails(int pos){
        try {
            FragmentManager manager = getActivity().getSupportFragmentManager();//getChildFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_frame_layout, new EscapeDetails(escapeList.get(pos)), ESCAPE_DETAILS_FRAGMENT);
            transaction.addToBackStack(null);
            transaction.commit();
            bottomNavigationView.setVisibility(View.GONE);
        }catch (NullPointerException npe){
            Log.d("LIST_FRAGMENT_OPEN_DET", "Escape details fragment error!");
            Toast.makeText(getActivity(), R.string.det_frag_err, Toast.LENGTH_LONG).show();
        }
    }

}
