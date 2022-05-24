package com.revature.storeApp.ui;


import java.util.Scanner;
import com.revature.storeApp.services.UserService;

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
        while (true) {
            System.out.println("[2]: Creating a new account...");
            //Prompt for a new username
            //If username is valid, prompt for password
            //If password is valid, ask to confirm
            //If account is created, return to start menu.
            break;
        }
    }
    private void login() {
        //Use another while loop:
        //Prompt for username, then prompt for password
        //Check username and password.
        //If username is invalid, print that no user with "username" exists
        //If password is invalid, print "incorrect password"
        throw new RuntimeException("Login needs implementation!");
    }
}



