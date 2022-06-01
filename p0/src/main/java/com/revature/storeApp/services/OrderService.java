package com.revature.storeApp.services;

import com.revature.storeApp.daos.LocationDAO;
import com.revature.storeApp.daos.OrderDAO;
import com.revature.storeApp.models.Item;
import com.revature.storeApp.models.Order;
import com.revature.storeApp.util.annotations.Inject;
import com.revature.storeApp.util.custom_exceptions.InvalidSQLException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/* Used to check order data and access DAOs */
public class OrderService {
    @Inject
    private final OrderDAO orderDAO;
    @Inject
    public OrderService(OrderDAO orderDAO){this.orderDAO=orderDAO;}


    public void deleteOrder(Order order){
        try {
            orderDAO.delete(order.getId());
        } catch (InvalidSQLException e){
            System.out.println(e.getMessage());
        }
    }
    public List<Order> getAllByStore(String store_id){
        List<Order> orderList = new ArrayList<Order>();
        try {
            orderList = orderDAO.getByStoreId(store_id);
        } catch (InvalidSQLException e) {
            System.out.println(e.getMessage());
        }
        return orderList;
    }
    public Order getById(String order_id){
        try {
            return orderDAO.getById(order_id);
        } catch (InvalidSQLException e){
            System.out.println(e.getMessage());
        }
        return new Order();
    }
    public List<Order> getAllByUser(String user_id){
        List<Order> orderList = new ArrayList<Order>();
        try {
            orderList = orderDAO.getByUserId(user_id);
        } catch (InvalidSQLException e) {
            System.out.println(e.getMessage());
        }
        return orderList;
    }

    public void save(Order order){
        try {
            orderDAO.save(order);
        } catch (InvalidSQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to save cart!");
        }
    }
    public boolean isInCart(Order order, String item_id){
        if (order.getCart().size()==0){
            return false;
        }
        boolean inCart = false;
        for (Item i : order.getCart()){
            if(i.getId().equals(item_id)){
                inCart=true;
                break;
            }
        }
        return inCart;
    }
    public boolean addToCart(String order_id, String item_id, int qty, int total){
        try{
            orderDAO.addToCart(order_id, item_id, qty, total);
            return true;
        } catch (InvalidSQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean updateCart(String order_id, String item_id, int qty, int total){
        try{
            orderDAO.updateCart(order_id, item_id, qty, total);
            return true;
        } catch (InvalidSQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean hasPendingCart(String user_id) {
        try {
            return orderDAO.pendingOrder(user_id);
        } catch (InvalidSQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public Order getPendingOrder(String user_id){
        try {
            return orderDAO.getPendingOrder(user_id);
        } catch (InvalidSQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public void checkOut(Order order){
        order.setDate(LocalDate.now(ZoneId.of("America/Montreal")).toString());
        //update inventory for purchased items
        try{
            orderDAO.checkOut(order);
            order.setPurchased(true);
        }   catch (InvalidSQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Order removeFromCart(Order order, String item_id) {
        try {
            orderDAO.removeFromCart(order, item_id);
            order.getCart().removeIf(item -> (item.getId().equals(item_id)));
        } catch (InvalidSQLException e) {
            System.out.println(e.getMessage());
        }
        return order;
    }

    public List<Order> getAllOrders(){
        List<Order> outList = new ArrayList<>();
        try {
            outList=orderDAO.getAll();
        } catch (InvalidSQLException e) {
            System.out.println(e.getMessage());
        }
        return outList;
    }
    public void sortByDate(List<Order> list){
        list.sort((o1,o2) -> o1.getDate().compareTo(o2.getDate()));
    }
    public void sortByPrice(List<Order> list){
        list.sort((o1,o2) -> Integer.valueOf(o1.getTotal()).compareTo(Integer.valueOf(o2.getTotal())));
    }
}