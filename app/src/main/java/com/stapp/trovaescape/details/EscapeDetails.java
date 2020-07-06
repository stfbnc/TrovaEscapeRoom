package com.stapp.trovaescape.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stapp.trovaescape.R;
import com.stapp.trovaescape.data.Constants;
import com.stapp.trovaescape.data.Escape;
import com.stapp.trovaescape.data.Room;

import java.util.ArrayList;
import java.util.Objects;

public class EscapeDetails extends Fragment {

    public static boolean isOpen = false;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.escape_details, container, false);

        TextView tvName = v.findViewById(R.id.escape_name);
        tvName.setText(currentEscape.getName());

        ImageButton imPhone = v.findViewById(R.id.escape_phone);
        imPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] tels = currentEscape.getPhone().split(Constants.PHONES_SEP);
                if(tels.length > 1) {
                    PopupMenu popupMenu = new PopupMenu(Objects.requireNonNull(getContext()), v);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.getTitle().toString()));
                            startActivity(i);
                            return true;
                        }
                    });
                    for (int t = 0; t < tels.length; t++)
                        popupMenu.getMenu().add(1, t, t, tels[t]);
                    popupMenu.show();
                } else {
                    Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + currentEscape.getPhone()));
                    startActivity(i);
                }
            }
        });

        ImageButton imWebsite = v.findViewById(R.id.escape_website);
        imWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!currentEscape.getWebsite().equals("")) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(currentEscape.getWebsite()));
                    startActivity(i);
                }
            }
        });

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

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        roomList.clear();
        roomList.addAll(currentEscape.getRoom());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isOpen = false;
    }

}
