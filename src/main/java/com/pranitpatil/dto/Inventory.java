package com.pranitpatil.dto;

import java.util.List;

public class Inventory {

    private List<Article> inventory;

    public List<Article> getInventory() {
        return inventory;
    }

    public void setInventory(List<Article> inventory) {
        this.inventory = inventory;
    }
}
