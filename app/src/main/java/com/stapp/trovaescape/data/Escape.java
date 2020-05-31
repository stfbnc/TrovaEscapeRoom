package com.stapp.trovaescape.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.content.Context.MODE_PRIVATE;

public class Escape {

    private Context context;
    private String code;
    private String name;
    private String shortName;
    private String address;
    private String phone;
    private String website;
    private LatLng coords;
    private ArrayList<Room> rooms;
    private boolean isFree;

    public Escape(Context context){
        this.context = context;
        this.code = "";
        this.name = "";
        this.shortName = "";
        this.address = "";
        this.phone = "";
        this.website = "";
        this.coords = null;
        this.rooms = new ArrayList<>();
        this.isFree = false;
    }

    public void setCode(String code) { this.code = code; }

    public String getCode() { return code; }

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public void setShortName(String shortName) { this.shortName = shortName; }

    public String getShortName() { return shortName; }

    public void setAddress(String address) { this.address = address; }

    public String getAddress() { return address; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getPhone() { return phone; }

    public void setWebsite(String website) { this.website = website; }

    public String getWebsite() { return website; }

    public void setCoords(LatLng coords) { this.coords = coords; }

    public LatLng getCoords() { return coords; }

    public void setRooms(ArrayList<Room> rooms) {
        for(int i = 0; i < rooms.size(); i++) {
            if(!rooms.get(i).getAvailabilities().equals("")) {
                setFree(true);
                break;
            }
        }
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        for(int i = 0; i < rooms.size(); i++){
            if(prefs.getString(rooms.get(i).getCode(), Constants.DOESNT_EXIST_STR).equals(Constants.DOESNT_EXIST_STR)){
                rooms.get(i).setDone(false);
            }else{
                rooms.get(i).setDone(true);
            }
        }
        this.rooms = rooms;
    }

    public ArrayList<Room> getRoom() {
        Collections.sort(rooms, new Comparator<Room>() {
            @Override
            public int compare(Room r1, Room r2) {
                return r1.getName().compareToIgnoreCase(r2.getName());
            }
        });
        return rooms;
    }

    public void setFree(boolean isFree) { this.isFree = isFree; }

    public boolean getFree() { return isFree; }

}