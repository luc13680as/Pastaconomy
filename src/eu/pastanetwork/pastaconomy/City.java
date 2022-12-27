package eu.pastanetwork.pastaconomy;

import com.sun.jdi.Value;
import eu.pastanetwork.pastaconomy.companies.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class City {
    String cityName;
    ArrayList<citizen> cityPopulation;
    ArrayList<company> cityCompanies;
    Market cityMarket;

    //Default constructors with defaults values
    public City(){
        this("Not Set",100);
    }
    public City(int tmpnumberOfCitizen){
        this("Not Set", tmpnumberOfCitizen);
    }
    public City(String yourname){
        this(yourname,100);
    }

    //Main constructor
    public City(String nameofcity,int numberOfCitizen){
        this.cityPopulation = new ArrayList<citizen>();
        this.cityCompanies = new ArrayList<company>();
        this.cityMarket = new Market(this);
        if(numberOfCitizen <= 1){
            return;
        }
        for(int i=0; i < numberOfCitizen; i++){
            cityPopulation.add(new citizen());
        }
        this.cityName = nameofcity;
        this.InitPopulation();

        this.giveMarketToCompanies(this.cityCompanies, this.cityMarket);
        this.giveMarketToCitizens(this.cityPopulation, this.cityMarket);

        for (company workingCompany : this.cityCompanies){
            workingCompany.produce();
        }
        int i=0;
        for (Market.Order o : this.cityMarket.displayOrders()){
            System.out.println("[" + i + "] " + o.type + " - " + o.item + " - " + o.amount + " - " + o.price + "€");
            i++;
        }
    }

    //Methods related to the city
    public int GetNumberPopulation(){
        return cityPopulation.size();
    }
    //public boolean SetNumberPopulation(String state, int number){}

    public String GetCityName(){
        return this.cityName;
    }
    //public boolean setCityName(String nameofcitytoset){}

    public int GetNumberCompanies(){
        return cityCompanies.size();
    }
    //public boolean SetNumberCompanies(String state, int number){}


    //Methods related to citizens
    private void InitPopulation(){
        for (citizen oneCitizen : cityPopulation){
            boolean state = oneCitizen.searchWork(this.cityCompanies);
            if (!state){
                oneCitizen.createCompany(this.cityCompanies);
            }
        }
    }
    /*public void AddCitizenToCity(citizen thechosenone){}
    public void RemoveCitizenFromCity(citizen thenotchosenone){}
    public void GetCitizen(int indexOfCitizen){}
    public void GetCitizenIndex(citizen citizenToGetIndexFrom){}*/

    //Methods related to Companies
    /*public void AddCompanyToCity(company thechosencompany){}
    public void RemoveCompanyFromCity(company thenotchosencompany){}
    public void GetCompany(int indexOfCompany){}
    public void GetCompanyIndex(company companyToGetIndexFrom){}*/

    //Methods related to Market
    private void giveMarketToCompanies(ArrayList<company> target, Market selectedMarket){
        for (company theCompany : target){
            theCompany.addMarket(selectedMarket);
        }
    }

    private void giveMarketToCitizens(ArrayList<citizen> target, Market selectedMarket){
        for (citizen theCitizen : target){
            theCitizen.addMarket(selectedMarket);
        }
    }

}
