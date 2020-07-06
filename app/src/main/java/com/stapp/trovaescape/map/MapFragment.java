package com.stapp.trovaescape.map;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stapp.trovaescape.R;
import com.stapp.trovaescape.data.Escape;
import com.stapp.trovaescape.db.DataManager;
import com.stapp.trovaescape.details.EscapeDetails;
import com.stapp.trovaescape.main.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String ESCAPE_DETAILS_FRAGMENT = "ESCAPE_DETAILS_FRAGMENT";

    private EditText searchEdit;
    private ArrayList<Escape> escapeList = new ArrayList<>();
    private HashMap<LatLng, Escape> markersCoords = new HashMap<>();
    private MapView mapView;
    private BottomNavigationView bottomNavigationView;
    public GoogleMap mMap = null;

    public MapFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.escape_map, container, false);

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
                if(mMap != null)
                    onMapReady(mMap);
            }
        });

        mapView = v.findViewById(R.id.map);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        escapeList.clear();
        markersCoords.clear();
        DataManager dm = new DataManager(getContext());
        escapeList.addAll(dm.getEscapes(MainActivity.filter));
        if(escapeList.size() > 0) {
            for (int i = 0; i < escapeList.size(); i++)
                markersCoords.put(escapeList.get(i).getCoords(), escapeList.get(i));

            ArrayList<Marker> markersList = new ArrayList<>();
            for (LatLng coords : markersCoords.keySet()) {
                MapMarker mapMarker = new MapMarker(markersCoords.get(coords), getContext());
                markersList.add(mMap.addMarker(new MarkerOptions().position(coords)
                        .icon(BitmapDescriptorFactory.fromBitmap(mapMarker.getMarker()))));
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markersList) {
                builder.include(marker.getPosition());
            }
            final LatLngBounds bounds = builder.build();
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int minMetric = Math.max(width, height);
            int padding = 80;
            if(!MainActivity.filter.equals(""))
                padding = (int)(minMetric * 0.2);
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    openDetails(marker);
                    return true;
                }
            });
        }else{
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.9109, 12.4818), 10));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        searchEdit.setText(MainActivity.filter);
        if(mMap != null)
            onMapReady(mMap);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        searchEdit.setText(MainActivity.filter);
        if(mMap != null)
            onMapReady(mMap);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    public void setBottomNavigationView(BottomNavigationView bottomNavigationView){
        this.bottomNavigationView = bottomNavigationView;
    }

    public void openDetails(Marker marker){
        try {
            FragmentManager manager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();//getChildFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_frame_layout, new EscapeDetails(markersCoords.get(marker.getPosition())),
                                ESCAPE_DETAILS_FRAGMENT);
            transaction.addToBackStack(null);
            transaction.commit();
            bottomNavigationView.setVisibility(View.GONE);
        }catch (NullPointerException npe){
            Toast.makeText(getActivity(), R.string.det_frag_err, Toast.LENGTH_LONG).show();
        }
    }

}
