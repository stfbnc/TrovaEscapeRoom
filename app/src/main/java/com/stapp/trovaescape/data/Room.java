package com.stapp.trovaescape.data;

import java.util.ArrayList;

public class Room {

    private String name;
    private String website;
    private ArrayList<String> prices;
    private ArrayList<String> availabilities;
    private ArrayList<String> tags;

    public Room(){
        this.name = "";
        this.website = "";
        this.prices = new ArrayList<>();
        this.availabilities = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public void setWebsite(String website) { this.website = website; }

    public String getWebsite() { return website; }

    public void setPrices(ArrayList<String> prices) { this.prices = prices; }

    public ArrayList<String> getPrices() { return prices; }

    public void setAvailabilities(ArrayList<String> availabilities) { this.availabilities = availabilities; }

    public ArrayList<String> getAvailabilities() { return availabilities; }

    public void setTags(ArrayList<String> tags) { this.tags = tags; }

    public ArrayList<String> getTags() { return tags; }

    public String getTagsFormatted(){
        StringBuilder t = new StringBuilder();
        for(int i = 0; i < tags.size(); i++){
            if(i != 0)
                t.append(" ");
            t.append("#");
            t.append(tags.get(i));
        }
        return t.toString();
    }

}
