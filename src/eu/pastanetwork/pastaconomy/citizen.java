package eu.pastanetwork.pastaconomy;

import eu.pastanetwork.pastaconomy.companies.company;
import eu.pastanetwork.pastaconomy.companies.woodCompany;

import java.util.ArrayList;

public class citizen {
    private String name = "Unknown";
    private String lastName = "Unknown";
    private int food = 20;
    private int health = 20;
    private int money = 0;
    //private inventory backpack = new inventory();

    private boolean hasJob = false;

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
                    System.out.println("The citizen has been recruited inside an company");
                    hasJob = true;
                    return true;
                }
                System.out.println("The citizen has been refused inside an company");
            }
        }
        return false;
    }

    public void createCompany(ArrayList<company> companyList){
        companyList.add(new woodCompany(this));
        System.out.println("The citizen created a company");
        return;
    }

    public void removeEmployeeFromCompany(company Company, citizen target){
        try {
            Company.DismissEmployee(target);
        }
        catch (IllegalArgumentException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
    }

    public void LeaveCompany(){
        hasJob = false;
    }
}
