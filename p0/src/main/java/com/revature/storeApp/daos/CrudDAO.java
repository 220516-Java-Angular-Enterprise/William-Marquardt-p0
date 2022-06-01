package com.revature.storeApp.daos;

import java.util.List;

//CRUD Dao; create, read, update, delete
public interface CrudDAO <T>{

    //create an object in the database
    void save(T obj);

    //obtain a database object T by using its ID
    T getById(String id);
    //obtain a list with all database entries of the appropriate type (i.e. users)
    List<T> getAll();

    //update database entry for object T
    void update(T obj);

    //delete a database entry
    void delete(String id);
}
