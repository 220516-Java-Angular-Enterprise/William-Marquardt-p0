package com.revature.storeApp.ui;

import com.revature.storeApp.models.Item;
import com.revature.storeApp.models.Location;
import com.revature.storeApp.models.Order;
import com.revature.storeApp.models.User;
import com.revature.storeApp.services.ItemService;
import com.revature.storeApp.services.LocationService;
import com.revature.storeApp.services.OrderService;
import com.revature.storeApp.services.UserService;
import com.revature.storeApp.util.annotations.Inject;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HistoryMenu implements IMenu{
    private final User user;

    @Inject
    private final LocationService locationService;
    @Inject
    private final ItemService itemService;
    @Inject
    private final OrderService orderService;
    @Inject
    private final UserService userService;
public HistoryMenu (User user, UserService userService, LocationService locService, ItemService itemService, OrderService orderService){
    this.user =user;
    this.userService = userService;
    this.locationService=locService;
    this.itemService=itemService;
    this.orderService=orderService;
}
    @Override
    public void start() {
        Scanner scan = new Scanner(System.in);
        exitHistory:
        {
            while (true) {
                System.out.println("-------------------------------------------------------");
                System.out.println("Reviewing order histories.  Please make a selection");
                System.out.println("[1]: User histories");
                System.out.println("[2]: Location histories");
                System.out.println("[x]: exit");
                System.out.println("-------------------------------------------------------");
                System.out.print("Enter:");
                String input=scan.nextLine();
                switch (input) {
                    case "1":
                        userHistories();
                        break;
                    case "2":
                        locationHistories();
                        break;
                    case "x":
                        System.out.println("Returning to the start menu...");
                        break exitHistory;
                    default:
                        System.out.println("Invalid input...");
                        break;

                }
            }
        }
    }
    private void userHistories(){
        System.out.println("Retrieving list of users...");
        Scanner scan = new Scanner(System.in);
        List<User> userList = userService.getAll();
        while (true){
            //list all users.  I despise how many times I have used this looping construct
            for (int i = 0; i < userList.size(); i++) {
                System.out.println("[" + (i + 1) + "] " + userList.get(i).getUsername());
            }
            System.out.print("\nEnter selection: ");
            //User selects a location.  Chomp the \n from input since we used nextInt
            int input = scan.nextInt() - 1;
            scan.nextLine();
            //if user selection is within range provided, open shopping menu with new Order
            if (input >= 0 && input < userList.size()) {
                User selectedUser = userList.get(input);
                System.out.println("Viewing User "+selectedUser.getUsername());
                List<Order> orderList = new ArrayList<>();
                orderList=orderService.getAllByUser(selectedUser.getId());
                if (orderList.size()==0){
                   System.out.println("No orders for this user.");
                   break;
                }
//                while (true){
//                            System.out.println("Display order history by [1]Date or [2]Price?");
//                    input=scan.nextInt();
//                    scan.nextInt();
//                    if (input == 1){
//                        orderService.sortByDate(orderList);
//                        break;
//                    } else if (input == 2){
//                        orderService.sortByPrice(orderList);
//                        break;
//                    } else System.out.println("Please enter 1 or 2.");
//                }
                System.out.println("Results:");
                for (Order o: orderList){
                    System.out.println("-------------------------------------------------------");
                    System.out.println(o.getDate()+": ");
                    System.out.println("Total: "+o.totalAsMoney());
                    for (Item i : o.getCart()){
                        System.out.println(i.toString());
                        System.out.println("Qty: "+i.getQty());
                    }
                    System.out.println("-------------------------------------------------------");
                }
                break;
            } else System.out.println("\nInvalid selection");
        }
    }
    private void locationHistories(){
        System.out.println("Retrieving list of locations...");
        Scanner scan = new Scanner(System.in);
        List<Location> locationList = locationService.getAll();
        while (true){
            //list all locations.  I despise how many times I have used this looping construct
            for (int i = 0; i < locationList.size(); i++) {
                System.out.println("[" + (i + 1) + "] \n" + locationList.get(i).toString());
            }
            System.out.print("\nEnter selection: ");
            //User selects a location.  Chomp the \n from input since we used nextInt
            int input = scan.nextInt() - 1;
            scan.nextLine();
            //if user selection is within range provided,
            if (input >= 0 && input < locationList.size()) {
                Location selectedLocation = locationList.get(input);
                System.out.println("Viewing Location "+selectedLocation.getName()+ " in "+selectedLocation.getCity()+","+selectedLocation.getState());
                List<Order> orderList = new ArrayList<>();
                orderList=orderService.getAllByStore(selectedLocation.getId());
                if (orderList.size()==0){
                    System.out.println("No orders for this location.");
                    break;
                }
//                while (true){
//                    System.out.println("Display order history by [1]Date or [2]Price?");
//                    input=scan.nextInt();
//                    scan.nextInt();
//                    if (input == 1){
//                        orderService.sortByDate(orderList);
//                        break;
//                    } else if (input == 2){
//                        orderService.sortByPrice(orderList);
//                        break;
//                    } else System.out.println("Please enter 1 or 2.");
//                }
                System.out.println("Results:");
                for (Order o: orderList){
                    if (o.isPurchased()) {
                        System.out.println("-------------------------------------------------------");
                        System.out.println(o.getDate() + ": ");
                        System.out.println("Total: " + o.totalAsMoney());
                        for (Item i : o.getCart()) {
                            System.out.println(i.toString());
                            System.out.println("Qty: "+i.getQty());
                        }
                        System.out.println("-------------------------------------------------------");
                    }
                }
                break;
            } else System.out.println("\nInvalid selection");
        }

    }
}
