package com.stapp.trovaescape.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stapp.trovaescape.R;
import com.stapp.trovaescape.data.Escape;
import com.stapp.trovaescape.data.Room;

import java.util.ArrayList;

public class EscapeDetails extends Fragment implements OnMapReadyCallback {

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static boolean isOpen = false;

    private MapView mapView;
    private Escape currentEscape;

    public EscapeDetails(Escape currentEscape) {
        this.currentEscape = currentEscape;
        isOpen = true;
    }

    /*public static EscapeDetails newInstance(){
        return new EscapeDetails();
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.escape_details, container, false);

        TextView tvName = v.findViewById(R.id.escape_name);
        tvName.setText(currentEscape.getName());

        TextView tvAddress = v.findViewById(R.id.escape_address);
        tvAddress.setText(currentEscape.getAddress());

        // TODO: link al telefono
        TextView tvPhone = v.findViewById(R.id.escape_phone);
        tvPhone.setText(currentEscape.getPhone());

        // TODO: link al sito
        TextView tvWebsite = v.findViewById(R.id.escape_website);
        tvWebsite.setText(currentEscape.getWebsite());

        ArrayList<Room> roomList = currentEscape.getRoom();

        final RecyclerView recRoom = v.findViewById(R.id.rooms_recycle);
        recRoom.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recRoom.setLayoutManager(layoutManager);
        RoomListAdapter adapter = new RoomListAdapter(roomList);
        recRoom.setAdapter(adapter);

        mapView = (MapView) v.findViewById(R.id.details_map);
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
        LatLng markerCoords = currentEscape.getCoords();

        float color;
        if(currentEscape.getFree())
            color = BitmapDescriptorFactory.HUE_GREEN;
        else
            color = BitmapDescriptorFactory.HUE_RED;
        //ArrayList<Marker> markersList = new ArrayList<>();
        //for(int i = 0; i < markersCoords.size(); i++){
        Marker marker = googleMap.addMarker(new MarkerOptions().position(markerCoords)
                                 .icon(BitmapDescriptorFactory.defaultMarker(color)));
            //marker.add(googleMap.addMarker(new MarkerOptions().position(markersCoords.get(i))
            //        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))));
        //}
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(marker.getPosition());
        //for (Marker marker : markersList) {
            //builder.include(marker.getPosition());
        //}
        final LatLngBounds bounds = builder.build();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 15));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
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
        isOpen = false;
    }

}
