package eu.pastanetwork.pastaconomy.companies;

import eu.pastanetwork.pastaconomy.citizen;

import java.util.ArrayList;

public class MiningCompany extends company{
    public MiningCompany(citizen creator) {
        super(creator);
        this.productionToolRequirement = "Pickaxe";
        this.possibleProductionList = new ArrayList<>();
        this.possibleProductionList.add("Coal");
        this.productionItem = "Coal";
        this.lowerProductionPossible = 32;
        this.upperProductionPossible = 128;
        this.efficiencyWithoutRequirement = 0.50f;
        this.baseSalary = 25;
    }
}
