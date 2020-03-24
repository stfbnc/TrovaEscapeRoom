package com.stapp.trovaescape.data;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Escape {

    private String name;
    private String address;
    private String phone;
    private String website;
    private LatLng coords;
    private ArrayList<Room> rooms;
    private boolean isFree;

    public Escape(){
        this.name = "";
        this.address = "";
        this.phone = "";
        this.website = "";
        this.coords = null;
        this.rooms = new ArrayList<>();
        this.isFree = false;
    }

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public void setAddress(String address) { this.address = address; }

    public String getAddress() { return address; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getPhone() { return phone; }

    public void setWebsite(String website) { this.website = website; }

    public String getWebsite() { return website; }

    public void setCoords(LatLng coords) { this.coords = coords; }

    public LatLng getCoords() { return coords; }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
        for(int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getAvailabilities().size() > 0) {
                setFree(true);
                break;
            }
        }
    }

    public ArrayList<Room> getRoom() { return rooms; }

    public void setFree(boolean isFree) { this.isFree = isFree; }

    public boolean getFree() { return isFree; }

    public String getAllTagsFormatted(){
        StringBuilder t = new StringBuilder();
        for(int i = 0; i < rooms.size(); i++) {
            t.append(rooms.get(i).getTagsFormatted());
            if(i != rooms.size()-1)
                t.append(" ");
        }
        return t.toString();
    }

}