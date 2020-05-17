package com.stapp.trovaescape.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.stapp.trovaescape.map.MapMarker;

import java.util.ArrayList;
import java.util.Locale;

public class EscapeDetails extends Fragment {// implements OnMapReadyCallback {

    // TODO: box per gli orari e i tag
    // TODO: filtro

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static boolean isOpen = false;

    //private MapView mapView;
    private Escape currentEscape;
    private ArrayList<Room> roomList = new ArrayList<>();
    private RoomListAdapter adapter;

    public EscapeDetails(Escape currentEscape) {
        this.currentEscape = currentEscape;
        isOpen = true;
    }

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

        ImageButton imPhone = v.findViewById(R.id.escape_phone);
        imPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + currentEscape.getPhone()));
                startActivity(i);
            }
        });

        ImageButton imWebsite = v.findViewById(R.id.escape_website);
        imWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(currentEscape.getWebsite()));
                startActivity(i);
            }
        });
        /*String ws = "<a href=\"" + currentEscape.getWebsite() + "\">" +
                    getContext().getString(R.string.site_string) + "</a>";
        tvWebsite.setText(Html.fromHtml(ws));
        tvWebsite.setMovementMethod(LinkMovementMethod.getInstance());
        tvWebsite.setLinksClickable(true);*/

        ImageButton imDirections = v.findViewById(R.id.escape_directions);
        final String dirData = "http://maps.google.com/maps?daddr="+
                         currentEscape.getCoords().latitude+","+
                         currentEscape.getCoords().longitude+
                         "("+currentEscape.getAddress()+")";
        imDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(dirData));
                i.setPackage("com.google.android.apps.maps");
                startActivity(i);
            }
        });

        roomList.addAll(currentEscape.getRoom());

        final RecyclerView recRoom = v.findViewById(R.id.rooms_recycle);
        recRoom.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recRoom.setLayoutManager(layoutManager);
        adapter = new RoomListAdapter(roomList);
        recRoom.setAdapter(adapter);

        /*mapView = (MapView) v.findViewById(R.id.details_map);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);*/

        return v;
    }

    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng markerCoords = currentEscape.getCoords();

        MapMarker mapMarker = new MapMarker(currentEscape.getFree(), getContext());
        Marker marker = googleMap.addMarker(new MarkerOptions().position(markerCoords)
                                 .title(currentEscape.getAddress())
                                 .icon(BitmapDescriptorFactory.fromBitmap(mapMarker.getDefaultMarker())));
        marker.showInfoWindow();
        //ArrayList<Marker> markersList = new ArrayList<>();
        //for(int i = 0; i < markersCoords.size(); i++){
        //Marker marker = googleMap.addMarker(new MarkerOptions().position(markerCoords)
        //                         .icon(BitmapDescriptorFactory.defaultMarker(color)));
            //marker.add(googleMap.addMarker(new MarkerOptions().position(markersCoords.get(i))
            //        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))));
        //}
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(marker.getPosition());
        //for (Marker marker : markersList) {
            //builder.include(marker.getPosition());
        //}
        final LatLngBounds bounds = builder.build();
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 18));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(marker.getPosition().latitude+0.00025,
                                                                marker.getPosition().longitude), 18));
    }*/

    @Override
    public void onResume() {
        super.onResume();
        //mapView.onResume();
        roomList.clear();
        roomList.addAll(currentEscape.getRoom());
        adapter.notifyDataSetChanged();
    }

    /*@Override
    public void onPause() {
        super.onPause();
        //mapView.onPause();
    }*/

    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }*/

    /*@Override
    public void onLowMemory() {
        super.onLowMemory();
        //mapView.onLowMemory();
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mapView.onDestroy();
        isOpen = false;
    }

}
