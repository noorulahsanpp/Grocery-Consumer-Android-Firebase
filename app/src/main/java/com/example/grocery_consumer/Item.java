package com.example.grocery_consumer;


public class Item {
    private String customer;
    private String phone;
    private String orderid;
//    private ArrayList<String> name;
//    private ArrayList<String > itemno;
//    private ArrayList<String > price;

    public Item() {

    }

    public Item(String customer, String phone, String orderid) {
        this.customer = customer;
        this.orderid = orderid;
        this.phone = phone;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }
}
