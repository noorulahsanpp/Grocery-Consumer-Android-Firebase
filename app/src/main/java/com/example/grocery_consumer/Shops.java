package com.example.grocery_consumer;

public class Shops {
    private String userid;
    private String storename;
    private String category;
    private String location;
    private String storeimage;

    public Shops() {

    }
    public Shops(String userid, String storename, String category, String location, String storeimage){
        this.userid = userid;
        this.storename = storename;
        this.category = category;
        this.location = location;
        this.storeimage = storeimage;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getlocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStoreimage() {
        return storeimage;
    }

    public void setStoreimage(String storeimage) {
        this.storeimage = storeimage;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}