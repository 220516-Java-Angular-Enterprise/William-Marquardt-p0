package com.revature.storeApp;

/* import stuff. */

import com.revature.storeApp.services.*;
import com.revature.storeApp.daos.*;
import com.revature.storeApp.ui.*;

/*This is the main class that starts the application */
public class MainDriver {
    public static void main(String[] args) {
        //use anonymous classes to start the application
        new StartMenu(new UserService(new UserDAO())).start();
    }
}
