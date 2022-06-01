package com.revature.storeApp.ui;


import java.util.Scanner;
import java.util.UUID;

import com.revature.storeApp.daos.ItemDAO;
import com.revature.storeApp.daos.LocationDAO;
import com.revature.storeApp.daos.OrderDAO;
import com.revature.storeApp.models.User;
import com.revature.storeApp.services.ItemService;
import com.revature.storeApp.services.LocationService;
import com.revature.storeApp.services.OrderService;
import com.revature.storeApp.services.UserService;
import com.revature.storeApp.util.custom_exceptions.InvalidUserInputException;

/*StartMenu is the menu users use when opening the program.  Allows users to sign in, sign up, or exit.*/
public class StartMenu implements IMenu {

    //We need a UserService, so we create one and attach it to our StartMenu via private final keywords.  This is an example of dependency injection.
    private final UserService userService;

    //Constructor that initializes the userService
    public StartMenu(UserService userService) {
        this.userService = userService;
    }

    public void start() {


        //start menu gives options to log in, sign up, or exit.  Signup and exit will be helper fns.


        //prompt for user input
        Scanner scanner = new Scanner(System.in);

        //In a loop, take user input and figure out what to do with it.
        //Included a labeled block for when user exits
        exit:
        {
            while (true) {
                //Display welcome message
                displayWelcomeMessage();
                //collect input
                String input = scanner.nextLine();
                //use a switch for each possible input
                switch (input) {
                    //case for login -> call login method
                    case "1":
                        login();
                        break;
                    //case for signup -> call signup method
                    case "2":
                        signUp();
                        break;
                    //case for exit -> exit while loop
                    case "x":
                        System.out.println("Goodbye...");
                        break exit;
                    //Default case occurs when an invalid input is used
                    default:
                        System.out.println("Invalid input...");
                        break;
                }


            }
        }
        }


    private void displayWelcomeMessage() {
        System.out.println("This is the welcome message. Please make a selection");
        System.out.println("[1]: login");
        System.out.println("[2]: sign up");
        System.out.println("[x]: exit");
    }

    private void signUp() {
        //Need user input, so create a scanner to set username and password
        Scanner scanner = new Scanner(System.in);
        String un;
        String pw;
        //Use a while loop similar to start() method in order to generate an account.
    completeExit:
    {
        while (true) {
            System.out.println("[2]: Creating a new account...");
            //Prompt for a new username. Validate.
            while (true) {

                System.out.print("Input username:");
                un=scanner.nextLine();
                try {
                    if (userService.isValidUsername(un) && userService.isNotDuplicateUsername(un)) {break;}
                } catch (InvalidUserInputException e) {
                    System.out.println(e.getMessage());
                }
            }
            //If username is valid, prompt for password
            while (true) {
                System.out.print("Input password:");
                pw=scanner.nextLine();
                try {
                    if (userService.isValidPassword(pw)) {
                        //Confirm password is valid
                        System.out.print("\nConfirm password:");
                        String confirm = scanner.nextLine();
                        //Re-enter password if they don't match
                        if(pw.equals(confirm)) break;
                        else System.out.println("Password and confirmation do not match.");
                    }
                } catch (InvalidUserInputException e) {
                    System.out.println(e.getMessage());
                }
            }
            //Now confirm the user's credentials

            confirmExit:
            {
                while (true) {
                    System.out.println("These will be your credentials:");
                    System.out.println("Username: "+un);
                    System.out.println("Password: "+pw);
                    System.out.print("Confirm? y/n: ");
                    String input=scanner.nextLine();
                    switch (input){
                        case "y":
                            //create new User object, register with database
                            User user = new User(UUID.randomUUID().toString(), un, pw, "DEFAULT");
                            userService.register(user);
                            System.out.println("New user "+un+" created.  Returning to login screen.");
                            //Exit the loop to return to login screen.
                            break completeExit;
                        case "n":
                            //break out and return to start of new credential entry
                            break confirmExit;
                        default:
                            System.out.println("Invalid input...");
                            //break the switch, restart the loop if input is invalid
                            break;

                    }
                }
            }
        }

    }
    }
    private void login() {
        String un;
        String pw;
        User user =new User();
        Scanner scan = new Scanner(System.in);

        //Use another while loop:
        while (true) {
            //Prompt for username, then prompt for password
            System.out.println("[1] Logging in...");
            System.out.print("Username:");
            un=scan.nextLine();
            System.out.print("\nPassword:");
            pw = scan.nextLine();
            //Check database for un, pw using userService
            try {
                user=userService.login(un, pw);
                if (user.getRole().equals("ADMIN")) new AdminMenu(user, new LocationService(new LocationDAO()), new ItemService(new ItemDAO())).start();
                else new MainMenu(user, userService, new LocationService(new LocationDAO()), new ItemService(new ItemDAO()), new OrderService(new OrderDAO())).start();
                break;

            } catch (InvalidUserInputException e) {
                System.out.println(e.getMessage());
            }
        }


    }
}



