package eu.pastanetwork.pastaconomy.companies;

import eu.pastanetwork.pastaconomy.citizen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class company {
    public String CompanyName = new String("Unknown");
    private ArrayList<citizen> employees;
    private int numberEmployees = 0;
    private int maxEmployees = 15;
    private int money = 0;

    public company(citizen thecreator){
        this(thecreator,"Unknown");
    }

    public company(citizen creator, String nameofcompany){
        this.employees = new ArrayList<citizen>();
        this.CompanyName = nameofcompany;
        employees.add(creator);
    }
    private void setName(String companyName){
        companyName=this.CompanyName;
    }
    public String getName() { return this.CompanyName; }


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

    /*public void DismissEmployee(citizen target){
        if (target instanceof citizen && ((citizen)target).equals(boss)){
            employees.remove(target);
            target.LeaveCompany();
        }
        else {
            throw new IllegalArgumentException("Only the owner of the company can remove an employee from a company");
        }
    }*/

    public void report(){
        System.out.println("Enterprise report: \nEmployees number:" + numberEmployees + "\nMax employees:" + maxEmployees + "\nMoney:" + money);
        return;
    }

    public abstract void Produce();
}

