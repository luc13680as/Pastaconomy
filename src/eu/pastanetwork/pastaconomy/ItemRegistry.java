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
        if (!this.HasItem(itemName)){
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
        this.AddItem("Iron");
        this.AddItem("Gold");
        this.AddItem("Pickaxe", 1);
        this.AddItem("Axe", 1);
        this.AddItem("Hoe", 1);
        this.AddItem("Fishing_Rod", 1);
        this.AddItem("Hammer", 1);
    }

    public List<Item> GetItemsList(){
        return this.items;
    }

    public boolean HasItem(String itemName){
        /*for (int i=0; i == items.size(); i++) {
            if (items.get(i).getName() == itemName) {
                return true;
            }
        }*/
        for (Item item : this.items){
            if (item.getName().equals(itemName)){
                return true;
            }
        }
        return false;
    }

    public Item getItem(String itemName){
        for (Item item : this.items){
            if (item.getName().equals(itemName)){
                return item;
            }
        }
        throw new NoSuchElementException("Item with name " + itemName + " not found in registry");
    }
}
