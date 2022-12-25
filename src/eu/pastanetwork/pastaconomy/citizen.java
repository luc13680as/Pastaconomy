package eu.pastanetwork.pastaconomy;

import eu.pastanetwork.pastaconomy.companies.*;

import java.util.ArrayList;

public class citizen {
    private String name;
    private String lastName;
    private Inventory backpack;
    private int food = 20;
    private int health = 20;
    private int money;
    private boolean hasJob = false;

    public citizen(){
        this("Unknown", "Unknown", 0);
    }

    public citizen(String providedName, String providedLastName, int providedMoney){
        this.name = providedName;
        this.lastName = providedLastName;
        this.money = providedMoney;
        this.backpack = new Inventory(9);
    }
    public void ReceiveMoney(int salary){
        this.money += salary;
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
    public void sayHello(){
        System.out.println("Hi ! My name is " + name + " " + lastName + "\nI have " + health + " HP and " + food + " food point !");
        return;
    }

    public boolean checkHasJob(){
        return this.hasJob;
    }

    private void setHasJob(boolean statusJob){
        this.hasJob = statusJob;
    }

    public boolean searchWork(ArrayList<company> companyList){
        if (this.hasJob == true){
            return true;
        }
        for(int i = 0; i < companyList.size(); i++){
            boolean companyResponse = companyList.get(i).isRecruiting();
            if (companyResponse){
                boolean companyStatus = companyList.get(i).recruitEmployees(this);
                if (companyStatus){
                    hasJob = true;
                    return true;
                }
            }
        }
        return false;
    }

    public void createCompany(ArrayList<company> companyList){
        companyList.add(new MiningCompany(this));
        this.hasJob = true;
        return;
    }

    public void LeaveCompany(){
        this.hasJob = false;
    }
}
