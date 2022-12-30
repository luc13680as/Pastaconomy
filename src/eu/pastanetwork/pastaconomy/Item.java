package eu.pastanetwork.pastaconomy;

public class Item {
    private String name;
    private String description;
    private String category;
    private int maxSlotQuantity;

    public Item(String nameProvided, String descriptionProvided, String categoryProvided, int maxSlotQuantity){
        this.name = nameProvided;
        this.description = descriptionProvided;
        this.category = categoryProvided;
        this.maxSlotQuantity = maxSlotQuantity;
    }

    public Item(String nameProvided, String descriptionProvided, int maxSlotQuantity){
        this(nameProvided, descriptionProvided, "default", maxSlotQuantity);
    }

    public Item(String nameProvided, String descriptionProvided){
        this(nameProvided, descriptionProvided, "default",64);
    }

    public Item(String nameProvided, int maxSlotQuantity){
        this(nameProvided, "No description available", "default", maxSlotQuantity);
    }

    public Item(String nameProvided){
        this(nameProvided, "No description available", "default", 64);
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

    public String getCategory(){
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
