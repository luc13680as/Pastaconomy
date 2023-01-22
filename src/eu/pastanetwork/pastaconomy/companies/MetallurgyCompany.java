package eu.pastanetwork.pastaconomy.companies;

import eu.pastanetwork.pastaconomy.citizen;

import java.util.ArrayList;
public class MetallurgyCompany extends company{
    public MetallurgyCompany(citizen creator){
        super(creator);
        this.productionToolRequirement = "Hammer";
        this.possibleProductionList = new ArrayList<>();
        this.possibleProductionList.add("Iron_ingot");
        this.possibleProductionList.add("Gold_ingot");
        this.selectNewProductionItem();
        this.lowerProductionPossible = 8;
        this.upperProductionPossible = 32;
        this.efficiencyWithoutRequirement = 1f;
        this.baseSalary = 50;
        this.fixedCostOfProduction = 35;
    }
}
