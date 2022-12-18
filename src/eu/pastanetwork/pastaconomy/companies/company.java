package eu.pastanetwork.pastaconomy.companies;

import eu.pastanetwork.pastaconomy.citizen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class company {
    public String name = new String("Unknown");
    private citizen boss;
    private ArrayList<citizen> employees = new ArrayList<citizen>();
    private int numberEmployees = 0;
    private int maxEmployees = 5;
    private int money = 0;

    public company(citizen creator){
        this.setBoss(creator);
        return;
    }
    private void setName(String companyName){
        companyName=name;
    }
    public String getName() { return name; }

    private boolean setBoss(citizen newBoss){
        this.boss = newBoss;
        return true;
    }

    public citizen getBoss() { return boss;}


    public boolean isRecruiting(){
        if (this.numberEmployees+1 > maxEmployees){
            return false;
        }
        return true;
    }

    public boolean recruitEmployees(citizen recruit){
        Random r = new Random();
        int low = 1;
        int high = 101;
        int result = r.nextInt(high-low) + low;

        if ((this.numberEmployees+1 > maxEmployees) || (result < 30)) {
            return false;
        }
        else{
            employees.add(recruit);
            numberEmployees++;
            return true;
        }
    }

    public void report(){
        System.out.println("Enterprise report: \nEmployees number:" + numberEmployees + "\nMax employees:" + maxEmployees + "\nMoney:" + money);
        return;
    }
}

