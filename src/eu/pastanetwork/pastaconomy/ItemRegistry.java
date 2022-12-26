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

    public boolean AddItem(String itemName, String description ,int maxSlotQuantity){
        if (this.CheckItemExist(itemName)){
            return false;
        }
        this.items.add(new Item(itemName, description, maxSlotQuantity));
        return true;
    }

    public boolean AddItem(String itemName, int maxSlotQuantity){
        boolean state = AddItem(itemName, "No description available", maxSlotQuantity);
        return state;
    }

    public boolean AddItem(String itemName){
        boolean state = AddItem(itemName, "No description available", 64);
        return state;
    }

    private void InitRegistry(){
        this.AddItem("Wheat");
        this.AddItem("Fish");
        this.AddItem("Wood");
        this.AddItem("Stone");
        this.AddItem("Coal");
        this.AddItem("Iron_ore");
        this.AddItem("Iron_ingot");
        this.AddItem("Gold_ore");
        this.AddItem("Gold_ingot");
        this.AddItem("Pickaxe", 1);
        this.AddItem("Axe", 1);
        this.AddItem("Hoe", 1);
        this.AddItem("Fishing_rod", 1);
        this.AddItem("Hammer", 1);
    }

    private List<Item> GetItemsList(){
        return this.items;
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
