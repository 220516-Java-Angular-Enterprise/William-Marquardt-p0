package com.revature.storeApp.services;

/* Used to check user data (password, username, etc.) and access DAOs */
public class UserService {

    public boolean isValidUsername(String username){
        /*use regex that requires usernames have 8-20 chars.  I am not a regex wizard, so copied from Google*/
        return username.matches("^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$");
    }

    public boolean isValidPassword(String password){
        /* use a regex that searches for a password with at least 8 characters, a special character, and a number*/
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
    }
}
