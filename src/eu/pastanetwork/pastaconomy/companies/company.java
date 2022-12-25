package eu.pastanetwork.pastaconomy.companies;

import eu.pastanetwork.pastaconomy.Inventory;
import eu.pastanetwork.pastaconomy.citizen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class company {
    protected String companyName = new String("Unknown");
    protected ArrayList<citizen> employees;
    protected Inventory companyInventory;
    protected int maxEmployees = 5;
    protected float recruitingChances;
    protected int money = 0;

    //Variable for subclasses to define
    protected String productionToolRequirement;
    protected ArrayList<String> possibleProductionList;
    protected String productionItem;
    protected int lowerProductionPossible;
    protected int upperProductionPossible;
    protected float efficiencyWithoutRequirement;
    protected int baseSalary;

    public company(citizen thecreator){
        this(thecreator,"Unknown");
    }

    public company(citizen creator, String nameofcompany){
        this.employees = new ArrayList<citizen>();
        this.companyName = nameofcompany;
        this.companyInventory = new Inventory();
        this.employees.add(creator);
    }
    public void setName(String companyNameProvided){
        this.companyName = companyNameProvided;
    }
    public String getName() { return this.companyName; }

    public void ReceiveMoney(int moneyReceived){
        this.money += moneyReceived;
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

    public boolean isRecruiting(){
        if (this.employees.size()+1 > maxEmployees){
            return false;
        }
        return true;
    }

    public boolean recruitEmployees(citizen recruit){
        Random r = new Random();
        int low = 1;
        int high = 101;
        int result = r.nextInt(high-low) + low;

        if ((this.employees.size()+1 > maxEmployees) || (result < 30)) {
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
        Random r = new Random();
        int selectedIndex = r.nextInt(this.possibleProductionList.size());
        this.productionItem = this.possibleProductionList.get(selectedIndex);
    }

    protected int getProductionAmount(){
        Random r = new Random();
        return r.nextInt(this.upperProductionPossible-this.lowerProductionPossible) + this.lowerProductionPossible;
    }

    public void produce(){
        int productionQuantity = getProductionAmount();
        if(!isRequirementCompleted()){
            productionQuantity = (int)Math.round(productionQuantity * this.efficiencyWithoutRequirement);
        }
        productionQuantity = productionQuantity * employees.size();
        this.companyInventory.AddItemToInventory(productionItem, productionQuantity);
        this.companyInventory.Display();
    }

    public int getSalaryCost(){
        return this.baseSalary * this.employees.size();
    }

    public void paySalary(){
        int totalToPay = this.getSalaryCost();
        this.money -= totalToPay;
        for (citizen theemployee: this.employees){
            theemployee.ReceiveMoney(this.baseSalary);
        }
    }

    protected boolean isRequirementCompleted(){
        if (this.productionToolRequirement == null){
            return true;
        }
        if(!companyInventory.CheckItemExist(this.productionToolRequirement)){
            return false;
        }
        return true;
    }

    protected int getProductionCost(int production){
        return (int)Math.round(production / this.getSalaryCost());
    }
}

