package eu.pastanetwork.pastaconomy;

import eu.pastanetwork.pastaconomy.companies.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        System.out.println("Creating a bunch of citizens");
        ArrayList<citizen> population = new ArrayList<>();
        ArrayList<company> companyRegistry = new ArrayList<>();
        for (int i = 0; i < 30; i++){
            population.add(new citizen());
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
        City mycity = new City("Pasta-City",200);
        System.out.println("City: " + mycity.GetCityName() + " Population: " + mycity.GetNumberPopulation() + " Compagnies: " + mycity.GetNumberCompanies());

        Market myMarket = new Market(mycity);
        myMarket.placeOrder("buy",population.get(3),"Pickaxe",1,50);

        ArrayList<Market.Order> returnedList = myMarket.searchOrder("buy","Pickaxe");
        int i = 0;
        for (Market.Order o : returnedList){
            System.out.println("[" + i + "] " + o.type + " - " + o.item + " - " + o.amount + " - " + o.price + "€");
            i++;
        }
    }
}