package com.example.grocery_consumer;

public class Product{
   private String pid;
    private  Double price;
    private  String name;
    private  String image;
    private Integer quantity;

    public Product(){

    }
    public Product( String pid, String name, Double price,String image,Integer quantity){
        this.pid = pid;
        this.name = name;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
