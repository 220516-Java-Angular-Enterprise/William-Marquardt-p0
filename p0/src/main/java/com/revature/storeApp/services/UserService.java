package com.revature.storeApp.services;
import com.revature.storeApp.daos.UserDAO;
import com.revature.storeApp.models.User;
import com.revature.storeApp.util.custom_exceptions.InvalidSQLException;
import com.revature.storeApp.util.custom_exceptions.InvalidUserInputException;

import java.util.ArrayList;
import java.util.List;

/* Used to check user data (password, username, etc.) and access DAOs */
public class UserService {

    //inject a user DAO to handle accessing the database
    private final UserDAO userDAO;
    public UserService (UserDAO userDAO){this.userDAO=userDAO;}

//    use DAO for users to return the user corresponding to these credentials
    public User login(String un, String pw){
        User user = new User();
        //return all users from database
        List<User> users = userDAO.getAll();
        for (User u : users) {
            if (u.getUsername().equals(un)) {
                //If username matches, set params and then check password
                //This implementation lets isValidCredentials return different messages is un or pw are missing
                user.setId(u.getId());
                user.setUsername(u.getUsername());
                user.setRole(u.getRole());
                if (u.getPassword().equals(pw)){
                //if password matches, set password.
                    user.setPassword(u.getPassword());
                    break;
                }
            }
        }
        //validate user credentials before returning user
        return isValidCredentials(user);
    }
    //when querying a new user, need to check if it's a duplicate username
    public boolean isNotDuplicateUsername(String username){
            List<String> usernames = userDAO.getAllUsernames();
            if (usernames.contains(username)) throw new InvalidUserInputException("Username is already taken.");
            else return true;
    }
    //Used for validating new usernames
    public boolean isValidUsername(String username){
        /*use regex that requires usernames have 8-20 chars.*/
        if (username.matches("^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$")) return true;
        else throw new InvalidUserInputException("Invalid Username.  Usernames must be 8-20 characters long.");

    }
//Used for validating new passwords
    public boolean isValidPassword(String password){
        /* use a regex that searches for a password with at least 8 characters, a special character, and a number*/
        if (password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) return true;
        else throw new InvalidUserInputException("Invalid password.  Password must have minimum 8 characters, a number, and a special character.");
    }
    //Used to throw exceptions if User credentials aren't found in database
    private User isValidCredentials(User user){
        //If username is incorrect, database query will return null.  Throw exception
        if (user.getUsername() == null)
            throw new InvalidUserInputException("Incorrect username.");
        //If password is incorrect, database query will return null.  Throw exception
        else if (user.getPassword()==null) throw new InvalidUserInputException("Incorrect Password");

        return user;
    }
    public void register(User user){
    //use UserDAO to add user to the database
        userDAO.save(user);
    }

    public boolean updateName (String name, String id){
        try {
            userDAO.updateName(name, id);
            return true;
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    public boolean deleteUser(User user){
        try{
            userDAO.delete(user.getId());
            return true;
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    public List<User> getAll() {
        List<User> outList = new ArrayList<>();
        try {
            outList = userDAO.getAll();

        }   catch (InvalidSQLException e) {
            System.out.println(e.getMessage());
        }
        return outList;
    }
}
