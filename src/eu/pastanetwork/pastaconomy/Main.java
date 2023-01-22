package eu.pastanetwork.pastaconomy;

import eu.pastanetwork.pastaconomy.companies.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        City myCity = new City("Pasta-City",20000);
        System.out.println("City: " + myCity.GetCityName() + " Population: " + myCity.GetNumberPopulation() + " Compagnies: " + myCity.GetNumberCompanies());
        for(int i=1; i <= 365; i++){
            System.out.println("Simulating: Day " + i);
            myCity.update();
        }
        myCity.displayMarketOrders();
    }
}