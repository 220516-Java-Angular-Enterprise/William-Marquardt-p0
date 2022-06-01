package com.revature.storeApp.models;

import java.util.List;

public class Order {
    private String id;

    private String user_id;
    private String store_id;
    private String date;
    private int total=0;
    private boolean isPurchased=false;
    private List<Item> cart;
    public Order() {
        super();
    }

    public Order(String id, String user_id, String store_id, String date, int total, List<Item> cart, boolean isPurchased) {
        this.id = id;
        this.user_id = user_id;
        this.store_id = store_id;
        this.date = date;
        this.total = total;
        this.isPurchased = isPurchased;
        this.cart = cart;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Item> getCart() {
        return cart;
    }

    public void setCart(List<Item> cart) {
        this.cart = cart;
    }
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotal() {
        this.total=calcTotal();
        return this.total;
    }

    public String totalAsMoney(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.getTotal());

        if (sb.length()<=2){
            return "$"+sb+".00";
        } else
            sb.insert((String.valueOf(total).length()-2),'.');
        return "$"+sb;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }
    private int calcTotal(){
        int subtotal = 0;
        for (Item item : this.cart){
            subtotal+=item.getPrice()*item.getQty();
        }
        return subtotal;
    }
}
