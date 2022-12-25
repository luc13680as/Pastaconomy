package eu.pastanetwork.pastaconomy.companies;

import eu.pastanetwork.pastaconomy.citizen;

import java.util.ArrayList;

public class AgricultureCompany extends company{
    public AgricultureCompany(citizen creator){
        super(creator);
        this.productionToolRequirement = "Hoe";
        this.possibleProductionList = new ArrayList<>();
        this.possibleProductionList.add("Wheat");
        this.productionItem = "Wheat";
        this.lowerProductionPossible = 32;
        this.upperProductionPossible = 128;
        this.efficiencyWithoutRequirement = 0.50f;
        this.baseSalary = 25;
    }
}
