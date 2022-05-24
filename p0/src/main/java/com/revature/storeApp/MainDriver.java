package com.revature.storeApp;

/* import stuff. */

import com.revature.storeApp.services.UserService;

/*This is the main class that starts the application */
public class MainDriver {
    public static void main(String[] args) {
        /* use a UserService object to check username, password, and interact with data access objects (DAOs)*/
        UserService userService = new UserService();

    }
}
