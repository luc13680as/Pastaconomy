package eu.pastanetwork.pastaconomy;

import eu.pastanetwork.pastaconomy.companies.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        System.out.println("Creating a bunch of citizens");
        ArrayList<citizen> population = new ArrayList<>();
        ArrayList<company> companyRegistry = new ArrayList<>();

        City mycity = new City("Pasta-City",200);
        ArrayList<Market> markets = new ArrayList<>();
        markets.add(new Market(mycity));

        Market myMarket = new Market(mycity);
        for (int i = 0; i < 30; i++){
            population.add(new citizen(markets));
        }

        System.out.println("Trigger the search for work");
        for (int i = 0; i < population.size(); i++){
            boolean state = population.get(i).searchWork(companyRegistry);
            if (state == false){
                population.get(i).createCompany(companyRegistry);
            }
        }

        for (int i = 0; i < companyRegistry.size(); i++){
            companyRegistry.get(i).produce();
        }

        System.out.println("== Test part ==");
        System.out.println("City: " + mycity.GetCityName() + " Population: " + mycity.GetNumberPopulation() + " Compagnies: " + mycity.GetNumberCompanies());

        myMarket.placeOrder("buy",population.get(3),"Pickaxe",1,50);
        myMarket.placeOrder("sell",companyRegistry.get(0),"Coal",64,35);
        myMarket.placeOrder("sell",companyRegistry.get(0),"Coal",128,40);
        myMarket.placeOrder("sell",companyRegistry.get(0),"Coal",23,560);
        myMarket.placeOrder("sell",companyRegistry.get(0),"Coal",48,430);
        myMarket.placeOrder("sell",companyRegistry.get(0),"Coal",36,250);
        ArrayList<Market.Order> returnedList = myMarket.searchOrder("sell","Coal");

        int i = 0;
        for (Market.Order o : myMarket.displayOrders()){
            System.out.println("[" + i + "] " + o.type + " - " + o.item + " - " + o.amount + " - " + o.price + "€");
            i++;
        }
    }
}