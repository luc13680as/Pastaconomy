package eu.pastanetwork.pastaconomy.companies;

import eu.pastanetwork.pastaconomy.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public abstract class company implements IMoney, IOrderPlacer {
    protected String companyName;
    protected ArrayList<citizen> employees;
    protected Inventory companyInventory;
    protected int maxEmployees;
    protected BigDecimal money;
    protected Market market;

    //Variable for subclasses to define
    protected String productionToolRequirement;
    protected boolean productionToolRequestSended = false;
    protected ArrayList<String> possibleProductionList;
    protected String productionItem;
    protected int lowerProductionPossible;
    protected int upperProductionPossible;
    protected float efficiencyWithoutRequirement;
    protected int baseSalary;
    protected int fixedCostOfProduction;

    public company(citizen thecreator){
        this(thecreator,"Unknown", 1000);
    }

    public company(citizen creator, String nameofcompany, int money){
        this.employees = new ArrayList<>();
        this.companyName = nameofcompany;
        this.companyInventory = new Inventory();
        this.employees.add(creator);
        this.maxEmployees = 30;
        this.money = new BigDecimal(money).setScale(2, RoundingMode.HALF_EVEN);
    }
    public void setName(String companyNameProvided){
        this.companyName = companyNameProvided;
    }
    public String getName() { return this.companyName; }

    @Override
    public void ReceiveMoney(BigDecimal moneyReceived){
        if (moneyReceived.compareTo(new BigDecimal(0)) > 0){
            this.money = this.money.add(moneyReceived);
        }
    }
    @Override
    public boolean SpendMoney(BigDecimal moneyToSpend){
        if (moneyToSpend.compareTo(this.money) > 0){
            return false;
        }
        this.money = this.money.subtract(moneyToSpend);
        return true;
    }
    @Override
    public BigDecimal GetMoney(){
        return this.money;
    }

    public boolean isRecruiting(){
        if (this.employees.size()+1 > maxEmployees){
            return false;
        }
        return true;
    }

    public boolean recruitEmployees(citizen recruit){
        int result = ThreadLocalRandom.current().nextInt(101);

        if (!(this.isRecruiting()) || (result < 30)) {
            return false;
        }
        else{
            employees.add(recruit);
            return true;
        }
    }

    public void DismissEmployee(citizen target){
        this.employees.remove(target);
        target.LeaveCompany();
    }

    public void report(){
        System.out.println("Enterprise report: \nEmployees number:" + employees.size() + "\nMax employees:" + maxEmployees + "\nMoney:" + money);
    }
    /*
    This part concerns the production of goods and the payment of wages.
     */

    protected void selectNewProductionItem(){
        //Random r = new Random();
        //int selectedIndex = r.nextInt(this.possibleProductionList.size());
        int selectedIndex = ThreadLocalRandom.current().nextInt(this.possibleProductionList.size());
        this.productionItem = this.possibleProductionList.get(selectedIndex);
    }

    protected int getProductionAmount(){
        //Random r = new Random();
        //return r.nextInt(this.upperProductionPossible-this.lowerProductionPossible) + this.lowerProductionPossible;
        return ThreadLocalRandom.current().nextInt(this.upperProductionPossible-this.lowerProductionPossible) + this.lowerProductionPossible;
    }

    public int produce(){
        int productionQuantity = getProductionAmount();
        if(!isRequirementCompleted()){
            productionQuantity = (int)Math.round(productionQuantity * this.efficiencyWithoutRequirement);
        }
        productionQuantity = productionQuantity * employees.size();

        try {
            this.companyInventory.AddItemToInventory(productionItem, productionQuantity);
        } catch (Exception IllegalArgumentException){
            int maximumAllowed = this.companyInventory.GetMaxPossibleSpace(productionItem);
            if (maximumAllowed < productionQuantity){
                productionQuantity = maximumAllowed;
                this.companyInventory.AddItemToInventory(productionItem, productionQuantity);
            }
        }
        return productionQuantity;
    }

    public int getSalaryCost(){
        return this.baseSalary * this.employees.size();
    }

    public void paySalary(){
        int totalToPay = this.getSalaryCost();
        this.SpendMoney(BigDecimal.valueOf(totalToPay));
        for (citizen theemployee: this.employees){
            theemployee.ReceiveMoney(new BigDecimal(this.baseSalary));
        }
    }

    protected boolean isRequirementCompleted(){
        if (this.productionToolRequirement == null){
            return true;
        }
        if(!companyInventory.CheckItemExist(this.productionToolRequirement)){
            return false;
        }
        this.productionToolRequestSended = false;
        return true;
    }

    @Override
    public void addMarket(Market theMarket){
        this.market = theMarket;
    }

    @Override
    public boolean hasMarket(){
        return this.market != null;
    }
    @Override
    public boolean checkOrder(Market.Order theOrder, int quantity){
        if (!(theOrder.from.equals(this))) {
            return false;
        }
        if ((theOrder.type.equals(Market.Order.TypeOrder.SELL))){
            if (!(this.companyInventory.CheckItemExist(theOrder.item)) || (this.companyInventory.GetItemQuantity(theOrder.item) < theOrder.amount)){
                return false;
            }
        } else {
            BigDecimal priceToPay = theOrder.price.multiply(new BigDecimal(quantity));
            if((priceToPay.compareTo(this.money) >= 0) || (this.companyInventory.GetMaxPossibleSpace(theOrder.item) < quantity)){
                return false;
            }
        }
        return true;
    }
    @Override
    public boolean processOrder(Market.Order theOrder, int quantity, BigDecimal price){
        if (checkOrder(theOrder, quantity)){
            return false;
        }
        if(theOrder.type.equals(Market.Order.TypeOrder.SELL)){
            this.companyInventory.RemoveItemFromInventory(theOrder.item, quantity);
            BigDecimal priceToGet = price.multiply(BigDecimal.valueOf(quantity));
            this.ReceiveMoney(priceToGet);
        } else {
            BigDecimal priceToPay = price.multiply(BigDecimal.valueOf(quantity));
            this.SpendMoney(priceToPay);
            this.companyInventory.AddItemToInventory(theOrder.item, quantity);
        }
        return true;
    }

    public void sellProduction(int production){
        if (production <= 0){
            return;
        }
        int totalCost = this.getSalaryCost() + this.fixedCostOfProduction;
        BigDecimal productionCostPerUnit = new BigDecimal(totalCost);
        productionCostPerUnit = productionCostPerUnit.divide(BigDecimal.valueOf(production), 2, RoundingMode.HALF_EVEN);

        //Selling price without any profit
        BigDecimal sellingPrice = productionCostPerUnit;
        this.market.placeOrder("sell", this, this.productionItem, production, sellingPrice);
    }

    public void buyToolRequirement(){
        if(!(this.isRequirementCompleted()) && (!this.productionToolRequestSended)){
            int numberOrder = this.market.searchOrder("buy", this, this.productionToolRequirement).size();
            if (numberOrder == 0) {
                this.market.placeOrder("buy", this, this.productionToolRequirement, 1, BigDecimal.valueOf(1));
                this.productionToolRequestSended = true;
            }
        }
    }
}

