package com.example.grocery_consumer;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;


public class PreviousItem {

    private String storename;
    private String orderid;
    private String description;
    private ArrayList<String> name;
    private ArrayList<String> itemno;
    private ArrayList<String> image;
    private ArrayList<String> price;
    Date date;

    public PreviousItem() {

    }

    public PreviousItem(String storename, String orderid, String description, ArrayList<String> name, ArrayList<String> itemno, ArrayList<String> image, ArrayList<String> price, Date date ) {
        this.storename = storename;
        this.orderid = orderid;
        this.description = description;
        this.name = name;
        this.itemno = itemno;
        this.image = image;
        this.price = price;
        this.date= date;

    }
    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public ArrayList<String> getName() {
        return name;
    }

    public void setName(ArrayList<String> name) {
        this.name = name;
    }

    public ArrayList<String> getItemno() {
        return itemno;
    }

    public void setItemno(ArrayList<String> itemno) {
        this.itemno = itemno;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public ArrayList<String> getPrice() {
        return price;
    }

    public void setPrice(ArrayList<String> price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }


}
