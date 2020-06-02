package com.stapp.trovaescape.map;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

    /*public static MapFragment newInstance(){
        return new MapFragment();
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.escape_map);
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
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

    /*@Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    //public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //super.onActivityCreated(savedInstanceState);
        if(getActivity()!=null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }
    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        escapeList.clear();
        markersCoords.clear();
        DataManager dm = new DataManager(getContext());
        escapeList.addAll(dm.getEscapes(MainActivity.filter));
        Log.d("AAAA", MainActivity.filter);
        if(escapeList.size() > 0) {
            for (int i = 0; i < escapeList.size(); i++)
                markersCoords.put(escapeList.get(i).getCoords(), escapeList.get(i));

            //float color;
            ArrayList<Marker> markersList = new ArrayList<>();
            //for(int i = 0; i < markersCoords.size(); i++){
            for (LatLng coords : markersCoords.keySet()) {
                /*if(markersCoords.get(coords).getFree())
                    color = BitmapDescriptorFactory.HUE_GREEN;
                else
                    color = BitmapDescriptorFactory.HUE_RED;*/
                //markersList.add(mMap.addMarker(new MarkerOptions().position(coords)
                //                    .icon(BitmapDescriptorFactory.defaultMarker(color))));
                MapMarker mapMarker = new MapMarker(markersCoords.get(coords), getContext());
                markersList.add(mMap.addMarker(new MarkerOptions().position(coords)
                        .icon(BitmapDescriptorFactory.fromBitmap(mapMarker.getMarker()))));
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markersList) {
                builder.include(marker.getPosition());
            }
            final LatLngBounds bounds = builder.build();
            //LinearLayout mapLayout = getView().findViewById(R.id.map_layout);
            //mapLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //    @Override
            //    public void onGlobalLayout() {
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int minMetric = Math.max(width, height);
            int padding = 80;
            if(!MainActivity.filter.equals(""))
                padding = (int)(minMetric * 0.2);
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
            //    }
            //});
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
        //mapView.invalidate();
        searchEdit.setText(MainActivity.filter);
        if(mMap != null)
            onMapReady(mMap);
        Log.d("map onResume", "map onResume");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        searchEdit.setText(MainActivity.filter);
        if(mMap != null)
            onMapReady(mMap);
        Log.d("map onHiddenChanged", "map onHiddenChanged");
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
            Log.d("MAP_FRAGMENT_OPEN_DET", "Escape details fragment error!");
            Toast.makeText(getActivity(), R.string.det_frag_err, Toast.LENGTH_LONG).show();
        }
    }

    /*private void getMarkersCoordinates(){
        getEscapeList();

    }

    private void getEscapeList(){

    }*/

}
