package com.revature.storeApp.services;

import com.revature.storeApp.daos.LocationDAO;
import com.revature.storeApp.models.Item;
import com.revature.storeApp.models.Location;
import com.revature.storeApp.util.custom_exceptions.InvalidSQLException;
import com.revature.storeApp.util.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocationService {
    private final LocationDAO locationDAO;
    public LocationService(LocationDAO locDAO){this.locationDAO=locDAO;}

    public void register(Location loc){
        locationDAO.save(loc);
    }
//operations that update or modify existing data have a boolean return in case it doesn't work.
    public boolean updateName(String name, String id){
        try {
            locationDAO.updateName(name, id);
            return true;
        } catch (InvalidSQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public boolean updateInventory(String item_id, int qty, String store_id){
        try{
            locationDAO.updateInventory(item_id, qty, store_id);
        } catch (InvalidSQLException e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean deleteLocation(String id){
        try {
            locationDAO.delete(id);
            return true;
        } catch (InvalidSQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public List<Location> getAll(){
        return locationDAO.getAll();
    }
    public Location getById(String id){
        return locationDAO.getById(id);
    }
    public HashMap<String, Item> getStock(String id){
        HashMap<String, Item> stock = locationDAO.getStock(id);

        return stock;
    }
}
