package eu.pastanetwork.pastaconomy.companies;

import eu.pastanetwork.pastaconomy.citizen;

import java.util.ArrayList;

public class BlacksmithCompany extends company{
    public BlacksmithCompany(citizen creator){
        super(creator);
        this.productionToolRequirement = "Hammer";
        this.possibleProductionList = new ArrayList<>();
        this.possibleProductionList.add("Pickaxe");
        this.possibleProductionList.add("Axe");
        this.possibleProductionList.add("Hoe");
        this.possibleProductionList.add("Hammer");
        this.selectNewProductionItem();
        this.lowerProductionPossible = 2;
        this.upperProductionPossible = 10;
        this.efficiencyWithoutRequirement = 0.50f;
        this.baseSalary = 75;
        this.fixedCostOfProduction = 50;
    }
}
