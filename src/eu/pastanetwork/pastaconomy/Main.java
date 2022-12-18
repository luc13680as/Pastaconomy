package eu.pastanetwork.pastaconomy;

import eu.pastanetwork.pastaconomy.companies.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        System.out.println("Creating a bunch of citizens");
        ArrayList<citizen> population = new ArrayList<>();
        ArrayList<company> companyRegistry = new ArrayList<>();
        for (int i = 0; i < 20; i++){
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
            companyRegistry.get(i).report();
        }

        System.out.println("== Test part ==");

        City mycity = new City("Pasta-City",20000);
        System.out.println("City: " + mycity.GetCityName() + " Population: " + mycity.GetNumberPopulation() + " Compagnies: " + mycity.GetNumberCompanies());
    }
}