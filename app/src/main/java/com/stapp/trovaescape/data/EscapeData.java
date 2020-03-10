package com.stapp.trovaescape.data;

public class EscapeData {

    private String name = "";
    private String address = "";
    private String tags = "";
    private boolean free = false;

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public void setAddress(String address) { this.address = address; }

    public String getAddress() { return address; }

    public void setTags(String tags) { this.tags = tags; }

    public String getTags() { return tags; }

    public void setFree(boolean free) { this.free = free; }

    public boolean getFree() { return free; }

}
