package com.revature.storeApp.daos;

import com.revature.storeApp.models.Item;
import com.revature.storeApp.util.custom_exceptions.InvalidSQLException;
import com.revature.storeApp.util.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ItemDAO implements CrudDAO<Item>{
    Connection con = DatabaseConnection.getCon();
    @Override
    public void save(Item obj) {
        try{
            //preparedStatement objects are used to generate and execute lines of SQL script
            PreparedStatement ps = con.prepareStatement("INSERT INTO items (id, name, description, price) VALUES (?, ?, ?, ?)");
            ps.setString(1, obj.getId());
            ps.setString(2, obj.getName());
            ps.setString(3, obj.getDescription());
            ps.setInt(4, obj.getPrice());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred while writing data to the database.");
        }
    }

    @Override
    public Item getById(String id) {
        return null;
    }

    @Override
    public List<Item> getAll() {
        return null;
    }

    @Override
    public void update(Item obj) {

    }

    @Override
    public void delete(String id) {

    }
}
