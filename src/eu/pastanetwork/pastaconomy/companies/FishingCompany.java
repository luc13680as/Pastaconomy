package eu.pastanetwork.pastaconomy.companies;

import eu.pastanetwork.pastaconomy.citizen;

import java.util.ArrayList;

public class FishingCompany extends company{
    public FishingCompany(citizen creator) {
        super(creator);
        this.productionToolRequirement = "Fishing_rod";
        this.possibleProductionList = new ArrayList<>();
        this.possibleProductionList.add("Fish");
        this.selectNewProductionItem();
        this.lowerProductionPossible = 32;
        this.upperProductionPossible = 128;
        this.efficiencyWithoutRequirement = 0.50f;
        this.baseSalary = 25;
        this.fixedCostOfProduction = 25;
    }
}
