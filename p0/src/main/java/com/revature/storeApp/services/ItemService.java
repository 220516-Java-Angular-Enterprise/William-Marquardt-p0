package com.revature.storeApp.services;

import com.revature.storeApp.daos.ItemDAO;
import com.revature.storeApp.models.Item;

public class ItemService {

    private final ItemDAO itemDAO;
    public ItemService(ItemDAO itemDAO){this.itemDAO=itemDAO;}

    public void register(Item item){
        itemDAO.save(item);
    }
}
