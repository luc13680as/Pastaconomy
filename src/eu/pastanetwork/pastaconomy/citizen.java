package eu.pastanetwork.pastaconomy;

import eu.pastanetwork.pastaconomy.companies.*;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class citizen implements IOrderPlacer, IMoney{
    private String name;
    private String lastName;
    private Inventory backpack;
    private int food;
    private int health;
    private BigDecimal money;
    private boolean hasJob;
    private boolean foodRequestSended;
    private ArrayList<Market> markets;
    private static final ArrayList<Class<? extends company>> COMPANY_TYPES; // = new ArrayList<>();

    static {
        COMPANY_TYPES = new ArrayList<>();
        COMPANY_TYPES.add(AgricultureCompany.class);
        COMPANY_TYPES.add(FishingCompany.class);
        COMPANY_TYPES.add(ForestryCompany.class);
        COMPANY_TYPES.add(MiningCompany.class);
    }

    public citizen(){
        this("Unknown", "Unknown", 200);
    }

    public citizen(String providedName, String providedLastName, int providedMoney){
        this.name = providedName;
        this.lastName = providedLastName;
        this.money = new BigDecimal(providedMoney).setScale(2, RoundingMode.HALF_EVEN);
        this.food = 20;
        this.health = 20;
        this.hasJob = false;
        this.backpack = new Inventory(9);
        this.markets = new ArrayList<>();
    }
    @Override
    public void ReceiveMoney(BigDecimal moneyReceived){
        if (moneyReceived.compareTo(new BigDecimal(0)) > 0){
            this.money.add(moneyReceived);
        }
    }
    @Override
    public boolean SpendMoney(BigDecimal moneyToSpend){
        if (moneyToSpend.compareTo(this.money) > 0){
            return false;
        }
        this.money.subtract(moneyToSpend);
        return true;
    }
    @Override
    public BigDecimal GetMoney(){
        return this.money;
    }

    public int GetFood(){
        return this.food;
    }
    public void GainFood(int foodToGain){
        this.food += foodToGain;
        if (this.food > 20){
            this.food = 20;
        }
    }
    public void LoseFood(int foodToLose){
        this.food -= foodToLose;
        if (this.food < 0){
            this.food = 0;
            this.LoseHealth(1);
        }
    }
    public int GetHealth(){
        return this.health;
    }
    public void GainHealth(int healthToGain){
        this.health += healthToGain;
        if (this.health > 20){
            this.health = 20;
        }
    }
    public void LoseHealth(int healthToLose){
        this.health -= healthToLose;
        if (this.health < 0){
            this.health = 0;
        }
    }

    public boolean checkHasJob(){
        return this.hasJob;
    }

    public boolean searchWork(ArrayList<company> companyList){
        if (this.hasJob){
            return true;
        }
        for(company targetCompany : companyList){
            boolean companyResponse = targetCompany.recruitEmployees(this);
            if (companyResponse){
                hasJob = true;
                return true;
            }

        }
        return false;
    }

    public void createCompany(ArrayList<company> companyList){
        //Class<? extends company> companyType = COMPANY_TYPES.get(new Random().nextInt(COMPANY_TYPES.size()));
        Class<? extends company> companyType = COMPANY_TYPES.get(ThreadLocalRandom.current().nextInt(COMPANY_TYPES.size()));

        // Create a new instance of the selected company type
        try {
            companyList.add(companyType.getDeclaredConstructor(citizen.class).newInstance(this));
            this.hasJob = true;
        } catch (ReflectiveOperationException e) {
            companyList.add(new AgricultureCompany(this));
            this.hasJob = true;
        }
    }

    public void LeaveCompany(){
        this.hasJob = false;
    }

    @Override
    public void addMarket(Market theMarket){
        this.markets.add(theMarket);
    }
    @Override
    public boolean hasMarket(){
        return this.markets.size() > 0;
    }
    private boolean checkOrder(Market.Order theOrder, int quantity){
        if (!(theOrder.from.equals(this))) {
            return false;
        }
        if ((theOrder.type.equals(Market.Order.TypeOrder.SELL))){
            if (!(this.backpack.CheckItemExist(theOrder.item)) || (this.backpack.GetItemQuantity(theOrder.item) < theOrder.amount)){
                return false;
            }
        } else {
            BigDecimal priceToPay = theOrder.price.multiply(new BigDecimal(quantity));
            if((priceToPay.compareTo(this.money) >= 0) || (this.backpack.GetMaxPossibleSpace(theOrder.item) < quantity)){
                return false;
            }
        }
        return true;
    }
    @Override
    public boolean processOrder(Market.Order theOrder, int quantity){
        if (checkOrder(theOrder, quantity)){
            return false;
        }
        if(theOrder.type.equals(Market.Order.TypeOrder.SELL)){
            this.backpack.RemoveItemFromInventory(theOrder.item, quantity);
            BigDecimal priceToGet = theOrder.price.multiply(BigDecimal.valueOf(quantity));
            this.ReceiveMoney(priceToGet);
        } else {
            BigDecimal priceToPay = theOrder.price.multiply(BigDecimal.valueOf(quantity));
            this.SpendMoney(priceToPay);
            this.backpack.AddItemToInventory(theOrder.item, quantity);
        }
        return true;
    }

    public void buyNeedsOnMarket(){
        if (this.backpack.CheckItemExist("Fish")){
            this.foodRequestSended = false;
            return;
        }
        if (this.foodRequestSended){
            return;
        }
        BigDecimal minPrice = new BigDecimal(1+ThreadLocalRandom.current().nextInt(500));
        int quantityDesired = ThreadLocalRandom.current().nextInt(1, 64);
        //Market targetMarket = null;
        Market targetMarket = this.markets.get(0);
        /*for(Market theMarket : this.markets){
            ArrayList<Market.Order> sellingMarket = theMarket.searchOrder("sell", "Fish");
            for (Market.Order theOrder : sellingMarket){
                if(minPrice.compareTo(theOrder.price) > 0){
                    minPrice = theOrder.price;
                    targetMarket = theMarket;
                }
            }
        }*/
        if(targetMarket != null){
            targetMarket.placeOrder("buy", this,"Fish", quantityDesired, minPrice);
            this.foodRequestSended = true;
        }
    }
}
