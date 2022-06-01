package com.revature.storeApp.daos;

import com.revature.storeApp.models.User;
import com.revature.storeApp.util.custom_exceptions.InvalidSQLException;
import com.revature.storeApp.util.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserDAO implements CrudDAO<User>{
    Connection con = DatabaseConnection.getCon();


    @Override
    //generates an SQL query that adds a user to the database
    public void save(User user) {
        try{
            //preparedStatement objects are used to generate and execute lines of SQL script
            PreparedStatement ps = con.prepareStatement("INSERT INTO users (id, username, password, role) VALUES (?, ?, ?, ?)");
            ps.setString(1, user.getId());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setString(4,user.getRole());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred while obtaining data from the database.");
        }
    }

    public void update(User user){

    }

    @Override
    public void delete(String id) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM users where id = ?");
            ps.setString(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred while deleting data from the database");
        }
    }

    @Override
    public User getById(String id) {
        User user = new User();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users where id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                user = new User(rs.getString("id"), rs.getString("username"), rs.getString("password"), rs.getString("role"));
            }
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred while obtaining data from to the database.");
        }

        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();

        try {
            //SQL query to return all entries from users table
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users");
            ResultSet rs = ps.executeQuery();
//ResultSet is a row of values from the query.  rs.next() returns the next row
            while (rs.next()) {
                User user = new User(); // user -> null
                user.setId(rs.getString("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role")); //

                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return users;
    }
    public List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();

        try {
            //sql statement that returns all usernames from users table
            PreparedStatement ps = con.prepareStatement("SELECT username FROM users");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                usernames.add(rs.getString("username").toLowerCase());
            }
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred while obtaining data from to the database.");
        }

        return usernames;
    }
    public void updateName(String name, String id) {
        try {
            PreparedStatement ps = con.prepareStatement("Update users SET name = ? WHERE id = ?");
            ps.setString(1, name);
            ps.setString(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred while updating data from the database");
        }
    }

}
