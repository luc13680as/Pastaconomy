package eu.pastanetwork.pastaconomy;

import eu.pastanetwork.pastaconomy.companies.*;

import java.util.ArrayList;


public class City {
    private String cityName;
    private ArrayList<citizen> cityPopulation;
    private ArrayList<company> cityCompanies;
    private Market cityMarket;

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
        this.cityPopulation = new ArrayList<citizen>(numberOfCitizen);
        this.cityCompanies = new ArrayList<company>();
        this.cityMarket = new Market(this);
        if(numberOfCitizen <= 1){
            return;
        }
        for(int i=0; i < numberOfCitizen; i++){
            cityPopulation.add(new citizen());
        }
        System.out.println("Init completed");
        this.cityName = nameofcity;
        this.findWorkForCitizen();
        this.giveMarketToCompanies(this.cityCompanies, this.cityMarket);
        this.giveMarketToCitizens(this.cityPopulation, this.cityMarket);
    }
    public void update(){
        for (company theCompany : this.cityCompanies){
            theCompany.buyToolRequirement();
            theCompany.paySalary();

            theCompany.sellProduction(theCompany.produce());
        }
        for (citizen theCitizen : this.cityPopulation){
            theCitizen.buyNeedsOnMarket();
        }
        cityMarket.matchOrders();
    }

    //Methods related to the city
    public int GetNumberPopulation(){
        return cityPopulation.size();
    }
    //public boolean SetNumberPopulation(String state, int number){}

    public String GetCityName(){
        return this.cityName;
    }

    public int GetNumberCompanies(){
        return cityCompanies.size();
    }


    //Methods related to citizens
    private void findWorkForCitizen(){
        int i = 0;
        for (citizen oneCitizen : cityPopulation){
            System.out.println(i++);
            if (!oneCitizen.checkHasJob()) {
                boolean state = oneCitizen.searchWork(this.cityCompanies);
                if (!state){
                    oneCitizen.createCompany(this.cityCompanies);
                }
            }
        }
    }
    //Methods related to companies

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

    public void displayMarketOrders(){
        int i=0;
        for (Market.Order o : this.cityMarket.displayOrders()){
            System.out.println("[" + i + "] " + o.type + " - " + o.item + " - " + o.amount + " - " + o.price.stripTrailingZeros().toPlainString() + "???");
            i++;
        }
    }

}
