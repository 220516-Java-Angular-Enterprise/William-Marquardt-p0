package com.revature.storeApp.daos;

import com.revature.storeApp.models.Item;
import com.revature.storeApp.models.Location;
import com.revature.storeApp.models.Order;
import com.revature.storeApp.util.custom_exceptions.InvalidSQLException;
import com.revature.storeApp.util.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderDAO implements CrudDAO<Order>{

    Connection con = DatabaseConnection.getCon();
    @Override
    public void save(Order obj) {
        //Save order parameters
        try {
            //insert into orders table
            PreparedStatement ps = con.prepareStatement("INSERT INTO orders (id, user_id, store_id, date, total, purchased) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, obj.getId());
            ps.setString(2, obj.getUser_id());
            ps.setString(3, obj.getStore_id());
            ps.setDate(4, Date.valueOf(obj.getDate()));
            ps.setInt(5, obj.getTotal());
            ps.setBoolean(6, obj.isPurchased());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when saving to the database.");
        }

    }

    @Override
    public Order getById(String id) {
        Order order = new Order();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM orders where id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                order = new Order(rs.getString("id"),rs.getString("user_id"),rs.getString("store_id"),rs.getString("date"),rs.getInt("total"), getCart(id),rs.getBoolean("purchased"));
            }

        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when obtaining data from the database.");
        }
        return order;
    }

    @Override
    public List<Order> getAll() {
        Order order = new Order();
        List<Order> orders= new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM orders");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                order = new Order(rs.getString("id"),rs.getString("user_id"),rs.getString("store_id"),rs.getString("date"),rs.getInt("total"), getCart(rs.getString("id")),rs.getBoolean("purchased"));
                orders.add(order);
            }

        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when obtaining data from the database.");
        }
        return orders;
    }
    public List<Order> getByStoreId(String store_id) {
        Order order = new Order();
        List<Order> orders= new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM orders where store_id = ?");
            ps.setString(1, store_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                order = new Order(rs.getString("id"),rs.getString("user_id"),rs.getString("store_id"),rs.getString("date"),rs.getInt("total"), getCart(rs.getString("id")),rs.getBoolean("purchased"));
                orders.add(order);
            }

        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when obtaining data from the database.");
        }
        return orders;

    }
    public List<Order> getByUserId(String user_id) {
        Order order = new Order();
        List<Order> orders= new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM orders where user_id = ?");
            ps.setString(1, user_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                order = new Order(rs.getString("id"),rs.getString("user_id"),rs.getString("store_id"),rs.getString("date"),rs.getInt("total"), getCart(rs.getString("id")),rs.getBoolean("purchased"));
                orders.add(order);
            }

        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when obtaining data from the database.");
        }
        return orders;

    }

    @Override
    public void update(Order obj) {

    }
    public void addToCart(String order_id, String item_id, int qty, int total){
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO order_items (order_id, item_id, qty) VALUES (?, ?, ?)");
            ps.setString(1, order_id);
            ps.setString(2, item_id);
            ps.setInt(3, qty);
            ps.executeUpdate();


        } catch (SQLException e){
            throw new InvalidSQLException("An error occurred when updating data in the database.");
        }

    }
    public void updateCart(String order_id, String item_id, int qty, int total){
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE order_items SET qty = ? WHERE order_id = ? AND item_id = ?");
            ps.setInt(1, qty);
            ps.setString(2, order_id);
            ps.setString(3, item_id);

        } catch (SQLException e){
            throw new InvalidSQLException("An error occurred while updating the database.");
        }
    }
    public void removeFromCart(Order order, String item_id){
        try {
            PreparedStatement ps = con.prepareStatement("delete from order_items where item_id= ? and order_id = ?");
            ps.setString(2, order.getId());
            ps.setString(1, item_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when deleting data from the database.");
        }
    }

    @Override
    public void delete(String id) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM orders WHERE id = ?");
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e){
            throw new InvalidSQLException("An error occurred while deleting data in the database.");
        }
    }
    public boolean pendingOrder(String user_id){
        try{
            PreparedStatement ps = con.prepareStatement("SELECT id FROM orders WHERE user_id = ? AND purchased = ?");
            ps.setString(1, user_id);
            ps.setBoolean(2, false);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return true;
            } else return false;
        } catch (SQLException e){
            throw new InvalidSQLException("An error occurred while querying the database");
        }
    }

    public Order getPendingOrder(String user_id){
        Order order = new Order();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM orders where user_id = ? and purchased = ?");
            ps.setString(1, user_id);
            ps.setBoolean(2, false);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                order = new Order(rs.getString("id"),rs.getString("user_id"),rs.getString("store_id"),rs.getString("date"),rs.getInt("total"), getCart(rs.getString("id")),rs.getBoolean("purchased"));
            }

        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when obtaining data from the database.");
        }
        return order;

    }

    public void checkOut(Order order){
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE orders SET date=?, total=?, purchased=? WHERE id = ?");
            ps.setDate(1, Date.valueOf(order.getDate()));
            ps.setInt(2, order.getTotal());
            ps.setBoolean(3, true);
            ps.setString(4, order.getId());
            ps.executeUpdate();
            for (Item item : order.getCart()){
                ps = con.prepareStatement("UPDATE store_items SET qty = qty - ? where store_id = ? and item_id = ?");
                ps.setInt(1, item.getQty());
                ps.setString(2, order.getStore_id());
                ps.setString(3, item.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new InvalidSQLException("An error occurred while updating the database.");
        }
    }
    private List<Item> getCart(String id){
        List<Item> cart = new ArrayList<Item>();
        try {
            PreparedStatement ps = con.prepareStatement("select id, name, description, price, qty from order_items inner join items on order_items.item_id = items.id where order_id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Item outItem = new Item(rs.getString("id"), rs.getString("name"), rs.getString("description"), rs.getInt("price"),rs.getInt("qty"));
                cart.add(outItem);
            }
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when obtaining data from the database.");
        }
        return cart;
    }
}
