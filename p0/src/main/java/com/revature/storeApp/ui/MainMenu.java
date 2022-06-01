package com.revature.storeApp.ui;

import com.revature.storeApp.daos.ItemDAO;
import com.revature.storeApp.daos.LocationDAO;
import com.revature.storeApp.daos.OrderDAO;
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
import java.util.UUID;

///mainMenu is the menu the customer uses after logging in.
public class MainMenu {
    //Dependency injection: this menu interacts with a specific customer
    //Therefore, we create a User object when instantiate the class.
    private final User user;

    @Inject
    private final LocationService locationService;
    @Inject
    private final ItemService itemService;
    @Inject
    private final OrderService orderService;

    @Inject
    private final UserService userService;
    @Inject
    public MainMenu(User user, UserService userService, LocationService locService, ItemService itemService, OrderService orderService) {
        this.user=user;
        this.userService = userService;
        this.locationService=locService;
        this.itemService=itemService;
        this.orderService=orderService;
    }
    public void start() {

        Scanner scanner = new Scanner(System.in);

        exit:
        {
            while (true){
                System.out.println("-------------------------------------------------------");
                System.out.println("Main Menu");
                System.out.println("Please make a selection");
                System.out.println("[1]: Browse locations and make purchases");
                System.out.println("[2]: Review order histories");
                System.out.println("[x]: exit");
                System.out.println("-------------------------------------------------------");
                String input=scanner.nextLine();
                switch (input) {
                    case "1":
                        browseLocations();
                        break;
                    case "2":
                        reviewOrderHistory();
                        break;
                    case "x":
                        System.out.println("Returning to the start menu...");
                        break exit;
                    default:
                        System.out.println("Invalid input...");
                        break;
                }

            }
        }
    }

    private void browseLocations(){
        //use OrderService check if user has an existing order.  If so, return to it.
        Scanner scan = new Scanner(System.in);
//        resolveOldCart:
//        {
//            if (orderService.hasPendingCart(user.getId())) {
//                Order oldOrder = orderService.getPendingOrder(user.getId());
//
//                while (true) {
//                    System.out.println("You have an existing cart from a previous visit. Would you like return to it? (y/n)");
//                    System.out.print("Enter: ");
//                    String inputStr = scan.nextLine();
//                    switch (inputStr) {
//                        case "y":
//                            new ShoppingMenu(user, locationService.getById(oldOrder.getStore_id()), oldOrder, locationService, itemService, orderService).start();
//                        case "n":
//                            orderService.deleteOrder(oldOrder);
//                            break resolveOldCart;
//                        default:
//                            System.out.println("Invalid selection...");
//                    }
//                }
//
//            }
//        }
            System.out.println("[1]: Browse locations and make purchases...");
            //This should all go into the ShoppingMenu.
        while (true) {
            System.out.println("Please select a location from the list.");
            //get all locations, present as list.
            List<Location> locations = locationService.getAll();
            for (int i = 0; i < locations.size(); i++) {
                System.out.println("[" + (i + 1) + "] \n" + locations.get(i).toString());
            }
            System.out.print("\nEnter selection: ");
            //User selects a location.  Chomp the \n from input since we used nextInt
            int input = scan.nextInt() - 1;
            scan.nextLine();

            //if user selection is within range provided, open shopping menu with new Order
            if (input >= 0 && input < locations.size()) {
                Location selectedLocation = locations.get(input);
                new ShoppingMenu(user, selectedLocation, new Order(UUID.randomUUID().toString(), user.getId(), selectedLocation.getId(), LocalDate.now(ZoneId.of("America/Montreal")).toString(),0, new ArrayList<Item>(), false), locationService, itemService, orderService).start();
                break;

            } else System.out.println("\nInvalid location selection!");
        }

        }
    private void reviewOrderHistory(){
        System.out.println("[2] Review order histories...");
        new HistoryMenu(user, userService, locationService, itemService, orderService).start();
    }

}

/*After a login, display the main menu.

Required functionality:
-place orders
-view order history by customer (sortable by date/price)
-view order history by location (sortable by date/price)
-view location inventory
-shopping cart (purchase multiple products at once)

 */
