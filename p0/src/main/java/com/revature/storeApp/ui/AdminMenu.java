package com.revature.storeApp.ui;

import com.revature.storeApp.models.Item;
import com.revature.storeApp.models.Location;
import com.revature.storeApp.models.User;
import com.revature.storeApp.services.ItemService;
import com.revature.storeApp.services.LocationService;
import com.revature.storeApp.util.annotations.Inject;
import com.revature.storeApp.util.custom_exceptions.InvalidSQLException;

import java.util.*;

public class AdminMenu implements IMenu{

    @Inject
    private final User user;
    private final LocationService locationService;
    private final ItemService itemService;

    @Inject
    public AdminMenu(User user, LocationService locService, ItemService itemService) {
        this.user=user;
        this.locationService=locService;
        this.itemService=itemService;
    }
    @Override
    public void start() {

        Scanner scanner = new Scanner(System.in);

    exit:
        {
        while (true){
            System.out.println("This is the admin menu welcome message. Please make a selection");
            System.out.println("[1]: Add a location");
            System.out.println("[2]: Edit a location");
            System.out.println("[3]: Register a new product");
//            System.out.println("[4]: Edit a user");
//            System.out.println("[5]: Edit an existing product");
            System.out.println("[x]: exit");
            String input=scanner.nextLine();
            switch (input) {
                case "1":
                    addLocation();
                    break;
                case "2":
                    editLocation();
                    break;
                case "3":
                    addProduct();
                    break;
//                case "4":
//                    editUser();
//                    break;
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


    // menu for adding a new location
    private void addLocation(){
        //need another scanner and a new location to add:
        Scanner scan = new Scanner(System.in);
        Location loc = new Location();

        exit:
        {
            //While loop that prompts for location fields, checks to confirm
            //TODO: validation for location parameters.  Should check that is not duplicate city and state
            while (true) {
                System.out.println("[1]: Adding a location...");
                //Location has a unique ID:
                loc.setId(UUID.randomUUID().toString());
                System.out.print("Location name: ");
                 loc.setName(scan.nextLine());
                System.out.print("\nLocation city: ");
                loc.setCity(scan.nextLine());
                System.out.print("\nLocation state: ");
                loc.setState(scan.nextLine());
            //check to make sure our state is only 2 characters long
                if (!(loc.getState().length()==2)){
                    System.out.println("Invalid state.  Please use a 2-letter abbreviation for the state.");
                    continue;
                }

            confirm:
            {
                while (true){
                    System.out.println("\nPlease confirm the new location to be added (y/n):");
                    System.out.println("\n"+loc);
                    switch (scan.nextLine()) {
                        case "y":
                            locationService.register(loc);
                            System.out.println("Adding to database. Inventory will be initially empty.");
                            break exit;
                        case "n":
                            break confirm;
                        default:
                            System.out.println("Invalid input.");
                            break;
                    }
                }
            }
            }
        }



    }

    //menu for editing or deleting a location.
    //editing a location includes restocking inventory
    private void editLocation(){
        //new menu:
        exitEdit:
        {
            System.out.println("[2]: Edit a location...");
            System.out.println("This menu is for editing a location's properties.");
            Scanner scan = new Scanner(System.in);
            while (true) {
                System.out.println("Please select a location to edit.");
                //get all locations, present as list.
                List<Location> locations = locationService.getAll();
                for (int i = 0; i < locations.size(); i++) {
                    System.out.println("[" + (i + 1) + "] " + locations.get(i).getName());
                }
                System.out.print("\nEnter selection: ");
                //User selects a location.  Chomp the \n from input since we used nextInt
                int input = scan.nextInt() - 1;
                scan.nextLine();

                //if user selection is within range provided, prompt for new name
                if (input >= 0 && input < locations.size()) {
                    //get all doesn't return the inventory of a store, so we gotta go grab that.
                    //...it would probably be better if getAll also returned inventories.
                    Location selectedLocation = locations.get(input);
                    selectedLocation.setStock(locationService.getStock(selectedLocation.getId()));
                    //obtain the stock for this location from the database;

                    while (true) {
                        System.out.println("Editing selected location:");
                        System.out.println(selectedLocation.toString());
                        System.out.println("[1]: Rename location");
                        System.out.println("[2]: Restock an item at this location");
                        System.out.println("[3]: Delete location");
//                        System.out.println("[4]: Add a new item to this location");
                        System.out.println("[x]: exit");
                        System.out.print("Enter: ");
                        String inputStr = scan.nextLine();
                        //methods that update the location object return a location object so that we can update it as we make changes within this menu.
                        switch (inputStr) {
                            case "1":
                                renameLocation(selectedLocation);
                                break;
                            case "2":
                                restockLocation(selectedLocation);
                                break;
 //                           case "4":
 //                             addToLocation(selectedLocation);
//                              break;
                            case "3":
                                System.out.println("[3]: Deleting location. Are you sure? (y/n)");
                                System.out.print("Enter: ");
                                inputStr = scan.nextLine();
                                switch (inputStr) {
                                    case "y":
                                        System.out.println("Really? (y/n)");
                                        System.out.print("Enter: ");
                                        inputStr = scan.nextLine();
                                        switch (inputStr) {
                                            case "y":
                                                System.out.println("Deleting location...");
                                                if (locationService.deleteLocation(selectedLocation.getId())) {
                                                    System.out.println("Successfully deleted!");
                                                    break;
                                                } else {
                                                    System.out.println("Delete failed!");
                                                    break;
                                                }
                                            case "n":
                                                System.out.println("Cancelling delete.");
                                                break;
                                            default:
                                                System.out.println("Invalid input.");
                                                break;
                                        }
                                    case "n":
                                        System.out.println("Cancelling delete.");
                                        break;
                                    default:
                                        System.out.println("Invalid input.");
                                        break;
                                }

                        break;

                        case "x":
                            System.out.println("Returning to admin menu...");
                            break exitEdit;
                        default:
                            System.out.println("Invalid input...");
                            break;
                        }
                    }

                } else System.out.println("\nInvalid location selection!");

            }
                //
        }
    }

    private Location renameLocation(Location location){
        //Wondering if it'd be more optimal to pass the original scanner in as well...
        System.out.println("[1]: Rename location");
        Scanner scan = new Scanner (System.in);
        exitRename:
        {
            while (true) {
                System.out.println("Current location name: " + location.getName());
                System.out.print("Enter new name:");
                String input = scan.nextLine();
                String newName=input;
                System.out.println("New name is "+input+".  Confirm? (y/n)");
                input=scan.nextLine();
                switch (input) {
                    case "y":
                        if (locationService.updateName(newName, location.getId())) {
                            System.out.println("\nRename was successful!");
                            location.setName(newName);
                            return location;
                        } else {
                            System.out.println("Unable to update location...");
                            break;
                        }
                    case "n":
                        System.out.println("Canceling rename...");
                        break exitRename;
                }
            }
        }
        return location;
    }

    private Location addToLocation(Location location){
//        while (true) {
//            itemsList=itemService.getAll();
//            return Location;
//        }
        return location;
    }
    private Location restockLocation(Location location){
            System.out.println("[2]: Restock at this location...");
            Scanner scan = new Scanner(System.in);
            while (true) {
                //list all items in this location's inventory
                List<Item> itemList = new ArrayList<Item>();
                for (String itemKey : location.getStock().keySet()){
                    itemList.add(location.getStock().get(itemKey));
                }
                for (int i = 0; i < itemList.size(); i++) {
                    System.out.println("[" + (i + 1) + "] " + itemList.get(i).getName());
                }
                System.out.print("Enter selection:");
                int input = scan.nextInt() - 1;
                scan.nextLine();
                if (input >= 0 && input < itemList.size()) {
                    Item selectedItem = itemList.get(input);
                    while (true) {
                        System.out.println("Current stock for " + selectedItem.getName() + " is " + selectedItem.getQty());
                        System.out.print("Enter a new value: ");
                        input = scan.nextInt();
                        if (input >= 0){
                            try{
                                if (locationService.updateInventory(selectedItem.getId(), input, location.getId())){
                                    System.out.println("Inventory update successful!");
                                    selectedItem.setQty(input);
                                    location.getStock().put(selectedItem.getId(), selectedItem);
                                    return location;
                                }else {
                                    System.out.println("Inventory update unsuccessful.  Returning to edit menu.");
                                }

                            } catch (InvalidSQLException e) {
                                System.out.println(e.getMessage());
                            }
                        } else {System.out.println("Invalid quantity");}
                    }

                }else {System.out.println("Invalid selection.");}
            }
    }
    private void deleteLocation(Location location){

    }
    //menu for editing or deleting a user
    private void editUser(){
        System.out.println("Needs implementation...");
    }

    //menu for adding a product
    private void addProduct(){
        //need another scanner and a new location to add:
        Scanner scan = new Scanner(System.in);
        Item item = new Item();
        //For input validation, may need to retrieve all items
        exit:
        {
            //While loop that prompts for location fields, checks to confirm
            //TODO: input validation for location parameters.  Should check for duplicate item names as well
            while (true) {
                System.out.println("[4]: Registering a new product...");
                item.setId(UUID.randomUUID().toString());
                System.out.print("Product name: ");
                item.setName(scan.nextLine());
                System.out.print("\nProduct description: ");
                item.setDescription(scan.nextLine());
                System.out.print("\nProduct price (in cents): ");
                item.setPrice(scan.nextInt());
                scan.nextLine();

                confirm:
                {
                    while (true){
                        System.out.println("\nPlease confirm the new item to be added (y/n):");
                        System.out.println("\n"+item);
                        switch (scan.nextLine()) {
                            case "y":
                                itemService.register(item);
                                System.out.println("Adding item to database.");
                                break exit;
                            case "n":
                                break confirm;
                            default:
                                System.out.println("Invalid input.");
                                break;
                        }
                    }
                }
            }
        }



    }

}

