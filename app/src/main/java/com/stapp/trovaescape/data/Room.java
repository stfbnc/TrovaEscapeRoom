package com.stapp.trovaescape.data;

public class Room {

    private String code;
    private String name;
    private String website;
    private String prices;
    private String availabilities;
    private String tags;

    public Room(){
        this.code = "";
        this.name = "";
        this.website = "";
        this.prices = "";
        this.availabilities = "";
        this.tags = "";
    }

    public void setCode(String code) { this.code = code; }

    public String getCode() { return code; }

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public void setWebsite(String website) { this.website = website; }

    public String getWebsite() { return website; }

    public void setPrices(String prices) { this.prices = prices; }

    public String getPrices() { return prices; }

    public void setAvailabilities(String availabilities) { this.availabilities = availabilities; }

    public String getAvailabilities() { return availabilities; }

    public void setTags(String tags) { this.tags = tags; }

    public String getTags() { return tags; }

}
