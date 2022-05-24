package com.revature.storeApp.ui;

import com.revature.storeApp.models.User;

///mainMenu is the menu the customer uses after logging in.
public class MainMenu {
    //Dependency injection: this menu interacts with a specific customer
    //Therefore, we create a User object when instantiate the class.
    private final User user;
    public MainMenu (User user){ this.user= user;}

    public void start(){}
}

/*After a login, display the main menu.

Required functionality:
-place orders
-view order history by customer (sortable by date/price)
-view order history by location (sortable by date/price)
-view location inventory
-shopping cart (purchase multiple products at once)

 */
