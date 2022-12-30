package eu.pastanetwork.pastaconomy;

import eu.pastanetwork.pastaconomy.companies.*;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class citizen {
    private String name;
    private String lastName;
    private Inventory backpack;
    private int food;
    private int health;
    private int money;
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
        this.money = providedMoney;
        this.food = 20;
        this.health = 20;
        this.hasJob = false;
        this.backpack = new Inventory(9);
        this.markets = new ArrayList<>();
    }
    public void ReceiveMoney(int moneyReceived){
        if (moneyReceived > 0){
            this.money += moneyReceived;
        }
    }
    public boolean SpendMoney(int moneyToSpend){
        if (moneyToSpend > this.money){
            return false;
        }
        this.money -= moneyToSpend;
        return true;
    }
    public int GetMoney(){
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
        /*for(int i = 0; i < companyList.size(); i++){
            boolean companyResponse = companyList.get(i).isRecruiting();
            if (companyResponse){
                boolean companyStatus = companyList.get(i).recruitEmployees(this);
                if (companyStatus){
                    hasJob = true;
                    return true;
                }
            }
        }*/
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

    public void addMarket(Market theMarket){
        this.markets.add(theMarket);
    }


    public void buyNeedsOnMarket(){
        if (this.backpack.CheckItemExist("Fish")){
            this.foodRequestSended = false;
            return;
        }
        if (this.foodRequestSended){
            return;
        }
        BigDecimal minPrice = new BigDecimal(ThreadLocalRandom.current().nextInt(500));
        int quantityDesired = ThreadLocalRandom.current().nextInt(1, 64);
        Market targetMarket = null;
        for(Market theMarket : this.markets){
            ArrayList<Market.Order> sellingMarket = theMarket.searchOrder("sell", "Fish");
            for (Market.Order theOrder : sellingMarket){
                if(minPrice.compareTo(theOrder.price) > 0){
                    minPrice = theOrder.price;
                    targetMarket = theMarket;
                }
            }
        }
        if(targetMarket != null){
            targetMarket.placeOrder("buy", this,"Fish", quantityDesired, minPrice);
            this.foodRequestSended = true;
        }
    }
}
