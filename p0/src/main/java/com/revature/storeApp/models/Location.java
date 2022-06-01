package com.revature.storeApp.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Location {
    private String id;
    private String name;
    private String city;
    private String state;

    private HashMap<String, Item> stock;

//    private HashMap<Item, Integer> stock;
    //want to have the default constructor handy
    public Location(){
        super();
    }
public Location (String id, String name, String city, String state, HashMap<String, Item> stock){
    this.id = id;
    this.name = name;
    this.city = city;
    this.state = state;
    this.stock = stock;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public HashMap<String, Item> getStock() {
        return stock;
    }

    public void setStock(HashMap<String, Item> stock) {
        this.stock = stock;
    }

    //toString method override swiped from trainer-p0
    @Override
    public String toString() {
        return "Name: " + name + "\nCity: " + city + "\nState: " + state;
    }
}
