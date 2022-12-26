package eu.pastanetwork.pastaconomy;

import eu.pastanetwork.pastaconomy.companies.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class City {
    String cityName;
    ArrayList<citizen> cityPopulation;
    ArrayList<company> cityCompanies;
    ArrayList<Market> cityMarkets;

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
        if(numberOfCitizen <= 1){
            return;
        }
        for(int i=0; i < numberOfCitizen; i++){
            cityPopulation.add(new citizen());
        }
        this.cityName = nameofcity;
        this.InitPopulation();
        this.cityMarkets = new ArrayList<>();
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
    public void AddCitizenToCity(citizen thechosenone){}
    public void RemoveCitizenFromCity(citizen thenotchosenone){}
    public void GetCitizen(int indexOfCitizen){}
    public void GetCitizenIndex(citizen citizenToGetIndexFrom){}

    private void InitPopulation(){
        int i = 0;
        for (citizen oneCitizen : cityPopulation){
            boolean state = oneCitizen.searchWork(this.cityCompanies);
            if (!state){
                oneCitizen.createCompany(this.cityCompanies);
            }
            i++;
        }
    }

    //Methods related to Companies
    public void AddCompanyToCity(company thechosencompany){}
    public void RemoveCompanyFromCity(company thenotchosencompany){}

    public void GetCompany(int indexOfCompany){}
    public void GetCompanyIndex(company companyToGetIndexFrom){}

    //Methods related to Market
    public void CreateMarket(){
        Market theMarket = new Market(this);
        this.cityMarkets.add(theMarket);
    }

    public void DeleteMarket(){
        this.cityMarkets.remove(new Random().nextInt(this.cityMarkets.size()));
    }

}
