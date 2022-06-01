package com.revature.storeApp.ui;

import com.revature.storeApp.models.Item;
import com.revature.storeApp.models.Location;
import com.revature.storeApp.models.Order;
import com.revature.storeApp.models.User;
import com.revature.storeApp.services.ItemService;
import com.revature.storeApp.services.LocationService;
import com.revature.storeApp.services.OrderService;
import com.revature.storeApp.util.annotations.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ShoppingMenu implements IMenu{
    private final User user;

    @Inject
    private final LocationService locationService;
    @Inject
    private final ItemService itemService;
    @Inject
    private final OrderService orderService;

    private final Location location;
    private Order order;
    @Inject
    public ShoppingMenu(User user, Location location, Order order, LocationService locService, ItemService itemService, OrderService orderService) {
        this.user=user;
        this.location = location;
        this.order = order;
        this.locationService=locService;
        this.itemService=itemService;
        this.orderService=orderService;
    }

    @Override
    public void start() {
        Scanner scan = new Scanner(System.in);
        //if this order is a new order, it will be empty.  See if it's in the database. Register it if not.
//        if (order.getTotal()==0){
//            //If it's not in there, its id won't match the one obtained from a query.  Register if it isn't.
//            if (!(orderService.getById(order.getId()).getId().equals(order.getId()))) {
                orderService.save(order);
//            }
//        }

        List<Item> itemList = new ArrayList<>();
        //obtain stock from location
        for (String itemKey : location.getStock().keySet()){
            itemList.add(location.getStock().get(itemKey));
        }
        exitBrowse:
        {
            while (true) {
                System.out.println("Browsing store");
                System.out.println(location);
                System.out.println("-------------------------------------------------------");
                for (int i = 0; i < itemList.size(); i++) {
                    System.out.println("[" + (i + 1) + "] \n" + itemList.get(i).toString());
                    System.out.println("Qty: " + itemList.get(i).getQty());
                }
                System.out.println("-------------------------------------------------------");

                System.out.println("[1]: Add an item to your cart");
                System.out.println("[2]: Review cart and checkout");
                System.out.println("[x]: exit");
                System.out.print("Enter: ");
                String inputStr = scan.nextLine();

                switch (inputStr) {
                    case "1":
                        while (true) {
                            System.out.print("Which item?");
                            int input = scan.nextInt();
                            scan.nextLine();
                            if (!(input > 0 && input <= itemList.size())) {
                                System.out.println("Invalid selection");
                            } else {
                                Item selectedItem = itemList.get(input - 1);
                                while (true) {
                                    System.out.print("\nHow many? ");
                                    input = scan.nextInt();
                                    scan.nextLine();
                                    if (input==0){
                                        break;
                                    }
                                    else if (!(input <=location.getStock().get(selectedItem.getId()).getQty())) {
                                        System.out.println("Qty must not be more than available stock...");
                                    } else {
                                        addToCart(order, selectedItem, input);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        break;
                    case "2":
                        checkout(order);
                        break exitBrowse;
                    case "x":
                        System.out.println("Returning to main menu...");
                        if (!order.isPurchased()){
                            orderService.deleteOrder(order);
                        }
                        break exitBrowse;
                    default:
                        System.out.println("Invalid input...");
                        break;
                }
            }
        }
    }
    private void addToCart(Order order, Item item, int qty){
        //Check if item is in cart.
        if (orderService.isInCart(order, item.getId())){
        //item already in cart; UPDATE it rather than add it
            if (orderService.updateCart(order.getId(),item.getId(),qty,order.getTotal())){
                System.out.println("Updated "+item.getName()+" qty in cart.");
            }
        }else{
        //Otherwise, item must not be in cart. therefore INSERT it. update and add methods return true if successful
            if (orderService.addToCart(order.getId(),item.getId(),qty,order.getTotal())) {
                System.out.println("Added " + item.getName() + " to cart.");
            }
        }
        //finally, update our order object with correct quantity
        boolean inCart = false;
        for (int i=0; i < order.getCart().size(); i++){
            if (order.getCart().get(i).getId().equals(item.getId())){
                order.getCart().get(i).setQty(qty);
                //an item only occurs 1 time in a cart, so break loop once we find it
                inCart=true;
                break;
            }
        }
        if (!inCart){
            order.getCart().add(item);
        }
    }
    private void checkout(Order order){
        //allow users to put back items, then checkout
        Scanner scan = new Scanner(System.in);
        exitCart: {
            while (true) {
                //list all items in order:
                System.out.println("-------------------------------------------------------");
                System.out.println("Items in this order:");
                List<Item> itemList = order.getCart();
                for (int i = 0; i < order.getCart().size(); i++) {
                    System.out.println("[" + (i + 1) + "] " + itemList.get(i).toString()+" x "+itemList.get(i).getQty());
                }
                System.out.println("-------------------------------------------------------");
                System.out.println("[1]: Remove an item");
                System.out.println("[c]: checkout");
                System.out.println("[x]: return to previous menu");
                System.out.print("Enter: ");
                String inputStr = scan.nextLine();
                switch (inputStr) {
                    case "1":
                        while (true) {
                            System.out.print("Which item?");
                            int input = scan.nextInt();
                            scan.nextLine();
                            if (!(input > 0 && input <= itemList.size())) {
                                System.out.println("Invalid selection - please pick an item from the list");
                            } else {
                                Item selectedItem = itemList.get(input - 1);
                                orderService.removeFromCart(order, selectedItem.getId());
                                System.out.println("Item removed");
                                break;
                            }
                        }
                        break;
                    case "c":
                        if (order.getCart().size()==0){
                            System.out.println("Cart is empty!");
                            break exitCart;
                        }
                        orderService.checkOut(order);
                        System.out.println("Order purchased");
                        break exitCart;
                    case "x":
                        System.out.println("Returning to previous menu...");
                        break exitCart;
                    default:
                        System.out.println("Invalid input...");
                        break;
                }
            }
        }
    }
}
