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
import com.stapp.trovaescape.data.Escape;
import com.stapp.trovaescape.details.EscapeDetails;
import com.stapp.trovaescape.main.MainActivity;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private static final String ESCAPE_DETAILS_FRAGMENT = "ESCAPE_DETAILS_FRAGMENT";

    private EditText searchEdit;
    private ArrayList<Escape> escapeList;
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    public void setBottomNavigationView(BottomNavigationView bottomNavigationView){
        this.bottomNavigationView = bottomNavigationView;
    }

    private void getListAtStart(){
        escapeList.clear();

        escapeList = MainActivity.e;

        adapter.notifyDataSetChanged();
    }

    private void getFilteredList(CharSequence txt){
        escapeList.clear();

        ArrayList<Escape> a = MainActivity.e;
        for(int i = 0; i < a.size(); i++){

            if(a.get(i).getName().toLowerCase().contains(txt.toString().toLowerCase()))
                escapeList.add(a.get(i));
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
