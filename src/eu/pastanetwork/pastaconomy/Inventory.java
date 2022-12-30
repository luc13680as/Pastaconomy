package eu.pastanetwork.pastaconomy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private static class Slot{
        public Item item;
        public int quantity;

        Slot(Item item, int quantity){
            this.item = item;
            this.quantity = quantity;
        }
    }
    int maximumSizeInventory;
    private ArrayList<Slot> inventorySlots;
    private ItemRegistry registry;

    public Inventory(int sizeProvided){
        this.registry = ItemRegistry.getInstance();
        this.maximumSizeInventory = sizeProvided;
        this.inventorySlots = new ArrayList<>();
    }

    public Inventory(){
        this(27);
    }

    private int GetEmptySlots(){
        int sizeDifference = this.maximumSizeInventory - this.inventorySlots.size();
        if (sizeDifference < 0){
            throw new IllegalStateException("Inventory slots used is greater than the total number of slots");
        }
        return sizeDifference;
    }

    private Item ConvertStringToItem(String itemName){
        Item itemRequested = registry.GetItem(itemName);
        return itemRequested;
    }

    public int GetMaxPossibleSpace(String itemStringName){
        Item itemName = ConvertStringToItem(itemStringName);
        int maxPossibleSpace = this.GetEmptySlots() * itemName.getMaxSlotQuantity();

        for (Slot slot : this.inventorySlots){
            if(slot.item.equals(itemName) && slot.quantity < itemName.getMaxSlotQuantity()){
                int difference = itemName.getMaxSlotQuantity() - slot.quantity;
                maxPossibleSpace += difference;
            }
        }
        return maxPossibleSpace;
    }

    public void AddItemToInventory(String itemName, int quantityRequested){
        int quantityRemaining = quantityRequested;
        Item requestItem = this.ConvertStringToItem(itemName);
        int maximumItemSlotSize = requestItem.getMaxSlotQuantity();

        if(this.GetMaxPossibleSpace(itemName) < quantityRemaining){
            //System.out.println("!! WARNING !! - The quantity requested (" + quantityRequested + ") go beyond the maximum remaining space (" + this.GetMaxPossibleSpace(itemName) + ") !");
        }

        for (Slot slot : this.inventorySlots){
            if(slot.item.equals(requestItem)){
                int newQuantity = slot.quantity + quantityRemaining;
                if (newQuantity > maximumItemSlotSize){
                    slot.quantity = maximumItemSlotSize;
                    quantityRemaining = newQuantity - maximumItemSlotSize;
                }
                else {
                    slot.quantity = newQuantity;
                    return;
                }
            }
        }
        while(quantityRemaining > 0){
            if(this.GetEmptySlots() == 0){
                throw new IllegalArgumentException("Not enough space in the inventory - Deleting " + quantityRemaining + " items !" );
            }
            if(quantityRemaining > maximumItemSlotSize){
                Slot theNewSlots = new Slot(requestItem,maximumItemSlotSize);
                this.inventorySlots.add(theNewSlots);
                quantityRemaining -= maximumItemSlotSize;
            }
            else{
                Slot theNewSlots = new Slot(requestItem,quantityRemaining);
                this.inventorySlots.add(theNewSlots);
                quantityRemaining = 0;
            }
        }
    }

    public int GetItemQuantity(String itemName){
        int itemCounter = 0;
        Item requestedItem = this.ConvertStringToItem(itemName);
        for (Slot slot : this.inventorySlots){
            if(slot.item.equals(requestedItem)){
                itemCounter += slot.quantity;
            }
        }
        return itemCounter;
    }

    public boolean CheckItemExist(String itemName){
        Item requestedItem = this.ConvertStringToItem(itemName);
        for (Slot slot : this.inventorySlots){
            if(slot.item.equals(requestedItem)){
                return true;
            }
        }
        return false;
    }

    public void RemoveItemFromInventory(String itemName, int quantity){
        Item requestItem = this.ConvertStringToItem(itemName);
        int quantityRemaining = quantity;
        for (int i= this.inventorySlots.size() -1; i >= 0 ; i--){
            Slot slot = this.inventorySlots.get(i);
            if (slot != null && slot.item.equals(requestItem)){
                int newQuantity = slot.quantity - quantityRemaining;
                if (newQuantity <= 0){
                    quantityRemaining -= slot.quantity;
                    this.inventorySlots.remove(i);
                }
                else {
                    slot.quantity = newQuantity;
                    quantityRemaining = 0;
                }
            }
        }
    }

    public void Display(){
        int i = 0;
        for (Slot slot : this.inventorySlots){
            System.out.println("[" + i + "] " + slot.item.getName() + " - " + slot.quantity);
            i++;
        }
    }
}