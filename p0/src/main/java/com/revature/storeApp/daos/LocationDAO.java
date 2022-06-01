package com.revature.storeApp.daos;

import com.revature.storeApp.models.Item;
import com.revature.storeApp.models.Location;
import com.revature.storeApp.models.User;
import com.revature.storeApp.util.custom_exceptions.InvalidSQLException;
import com.revature.storeApp.util.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocationDAO implements CrudDAO<Location>{
    Connection con = DatabaseConnection.getCon();
    @Override
    public void save(Location loc) {
        try{
            //preparedStatement objects are used to generate and execute lines of SQL script
            PreparedStatement ps = con.prepareStatement("INSERT INTO stores (id, name, city, state) VALUES (?, ?, ?, ?)");
            ps.setString(1, loc.getId());
            ps.setString(2, loc.getName());
            ps.setString(3, loc.getCity());
            ps.setString(4, loc.getState());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred while writing data to the database.");
        }
    }

    @Override
    public Location getById(String id) {
        Location loc = new Location();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM stores where id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                loc = new Location(rs.getString("id"), rs.getString("name"), rs.getString("city"), rs.getString("state"), getStock(id));
            }

        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when obtaining data from the database.");
        }
        return loc;
    }

    @Override
    public List<Location> getAll() {
        List<Location> locations = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM stores");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                locations.add(new Location(id, rs.getString("name"), rs.getString("city"), rs.getString("state"),getStock(id)));
            }

        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when obtaining data from the database.");
        }

        return locations;
    }

    @Override
    public void update(Location obj) {

    }

    public void updateName(String name, String id) {
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE stores SET name = ? WHERE id = ?");
            ps.setString(1, name);
            ps.setString(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred while updating data in the database.");
        }

    }
    @Override
    public void delete(String id) {
        try {
            PreparedStatement ps = con.prepareStatement("delete from stores where id = ?");
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e){
            throw new InvalidSQLException("An error occurred while deleting data in the database.");
        }
    }

    public HashMap<String, Item> getStock(String id){
        HashMap<String, Item> stock = new HashMap<String, Item>();
        try {
            PreparedStatement ps = con.prepareStatement("select id, name, description, price, qty from store_items inner join items on store_items.item_id = items.id where store_id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Item outItem = new Item(rs.getString("id"), rs.getString("name"), rs.getString("description"), rs.getInt("price"),rs.getInt("qty"));
                stock.put(outItem.getId(),outItem);
           }
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when obtaining data from the database.");
        }
        return stock;
    }
    public void updateInventory(String item_id, int qty, String store_id) {
        try {
            PreparedStatement ps = con.prepareStatement("update store_items set qty = ? where store_id = ? and item_id = ?");
            ps.setString(2, store_id);
            ps.setString(3, item_id);
            ps.setInt(1, qty);
            ps.executeUpdate();

        } catch (SQLException e){
            throw new InvalidSQLException("An error occurred when updating data in the database");
        }
    }
}
