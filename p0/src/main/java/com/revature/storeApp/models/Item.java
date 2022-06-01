package com.revature.storeApp.models;

public class Item {
    private String id;
    private String name;
    private String description;
    private int price;
    private int qty;


    public Item (){super();}
    public Item (String id, String name, String description, int price, int qty){
        this.id = id;
        this.name = name;
        this.description=description;
        this.price = price;
        this.qty = qty;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(price);
        sb.insert((String.valueOf(price).length()-2),'.');
        return "Name: " + name + "\nDescription: " + description + "\nPrice: $" + sb.toString();
    }

}
