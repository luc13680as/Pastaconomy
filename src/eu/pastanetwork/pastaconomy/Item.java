package eu.pastanetwork.pastaconomy;

public class Item {
    private String name;
    private String description;
    private int maxSlotQuantity;

    public Item(String nameProvided, String descriptionProvided, int maxSlotQuantity){
        this.name = nameProvided;
        this.description = descriptionProvided;
        this.maxSlotQuantity = maxSlotQuantity;
    }

    public Item(String nameProvided, String descriptionProvided){
        this(nameProvided, descriptionProvided, 64);
    }

    public Item(String nameProvided, int maxSlotQuantity){
        this(nameProvided, "No description available", maxSlotQuantity);
    }

    public Item(String nameProvided){
        this(nameProvided, "No description available", 64);
    }

    public String getName(){
        return this.name;
    }
    public String getDescription(){
        return this.description;
    }
    public int getMaxSlotQuantity(){
        return this.maxSlotQuantity;
    }
}
