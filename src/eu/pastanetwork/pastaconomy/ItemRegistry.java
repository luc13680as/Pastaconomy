package eu.pastanetwork.pastaconomy;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ItemRegistry {
    private static ItemRegistry instance;
    private ArrayList<Item> items;

    public static ItemRegistry getInstance(){
        if (instance == null){
            instance = new ItemRegistry();
        }
        return instance;
    }

    private ItemRegistry(){
        this.items = new ArrayList<>();
        InitRegistry();
    }

    public boolean AddItem(String itemName, String description, String category, int maxSlotQuantity){
        if (this.CheckItemExist(itemName)){
            return false;
        }
        this.items.add(new Item(itemName, description, category, maxSlotQuantity));
        return true;
    }

    public boolean AddItem(String itemName, String description, String category){
        boolean state = AddItem(itemName, description, category, 64);
        return state;
    }

    public boolean AddItem(String itemName, int maxSlotQuantity){
        boolean state = AddItem(itemName, "No description available", "default", maxSlotQuantity);
        return state;
    }

    public boolean AddItem(String itemName){
        boolean state = AddItem(itemName, "No description available", "default",64);
        return state;
    }

    private void InitRegistry(){
        this.AddItem("Wheat", "No description provided", "Raw material");
        this.AddItem("Fish", "No description provided", "Food");
        this.AddItem("Wood", "No description provided", "Raw material");
        this.AddItem("Stone", "No description provided", "Raw material");
        this.AddItem("Coal", "No description provided", "Raw material");
        this.AddItem("Iron_ore", "No description provided", "Raw material");
        this.AddItem("Iron_ingot", "No description provided", "Raw material");
        this.AddItem("Gold_ore", "No description provided", "Raw material");
        this.AddItem("Gold_ingot", "No description provided", "Raw material");
        this.AddItem("Pickaxe", "No description provided", "Tool", 1);
        this.AddItem("Axe", "No description provided", "Tool", 1);
        this.AddItem("Hoe", "No description provided", "Tool",1);
        this.AddItem("Fishing_rod", "No description provided", "Tool",1);
        this.AddItem("Hammer", "No description provided", "Tool",1);
    }

    private Item FindItem(String itemName){
        for (Item itemLoop : this.items){
            if (itemLoop.getName().equals(itemName)){
                return itemLoop;
            }
        }
        return null;
    }

    public boolean CheckItemExist(String itemName){
        return this.FindItem(itemName) != null;
    }

    public Item GetItem(String itemName){
        Item item = this.FindItem(itemName);
        if(item == null){
            throw new NoSuchElementException("Item with name " + itemName + " not found in registry");
        }
        return item;
    }
}
