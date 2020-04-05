package com.stapp.trovaescape.map;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stapp.trovaescape.R;
import com.stapp.trovaescape.data.Escape;
import com.stapp.trovaescape.db.DataManager;
import com.stapp.trovaescape.main.MainActivity;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private ArrayList<Escape> escapeList = new ArrayList<>();
    private ArrayList<LatLng> markersCoords = new ArrayList<>();
    private MapView mapView;
    //private GoogleMap mMap;

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
        mapView = (MapView) v.findViewById(R.id.map);
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
        //mMap = googleMap;
        getMarkersCoordinates();

        ArrayList<Marker> markersList = new ArrayList<>();
        for(int i = 0; i < markersCoords.size(); i++){
            markersList.add(googleMap.addMarker(new MarkerOptions().position(markersCoords.get(i))));
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
        //int minMetric = Math.min(width, height);
        //int padding = (int) (minMetric * 10);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, 80));
        //    }
        //});
        /*mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                MarkerDetails markerDetails = new MarkerDetails(MapsActivity.this, marker);
                markerDetails.showDetails();
                return true;
            }
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        //getMarkersCoordinates();
        mapView.invalidate();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
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

    private void getMarkersCoordinates(){
        getEscapeList();
        for(int i = 0; i < escapeList.size(); i++)
            markersCoords.add(escapeList.get(i).getCoords());
    }

    private void getEscapeList(){
        escapeList.clear();
        DataManager dm = new DataManager(getContext());
        escapeList.addAll(dm.getEscapes(""));
    }

}
