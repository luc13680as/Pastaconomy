package eu.pastanetwork.pastaconomy;

import java.util.ArrayList;
import java.util.HashMap;

public class Inventory {
    int maximumSizeInventory;
    private HashMap<Item, Integer> inventorySlots;
    private ArrayList<Item> itemList;
    private ItemRegistry registry;

    public Inventory(int sizeProvided){
        this.maximumSizeInventory = sizeProvided;
        this.inventorySlots = new HashMap<Item, Integer>();
        this.registry = ItemRegistry.getInstance();
    }

    public Inventory(){
        this(27);
    }

    public void AddItemToInventory(String itemName){}

    public void GetItemFromInventory(String itemName){}

    public void RemoveItemFromInventory(String itemName){}
}