package eu.pastanetwork.pastaconomy.companies;

import eu.pastanetwork.pastaconomy.citizen;

import java.util.ArrayList;

public class MiningCompany extends company{
    public MiningCompany(citizen creator) {
        super(creator);
        this.productionToolRequirement = "Pickaxe";
        this.possibleProductionList = new ArrayList<>();
        this.possibleProductionList.add("Stone");
        this.possibleProductionList.add("Coal");
        this.possibleProductionList.add("Iron_ore");
        this.possibleProductionList.add("Gold_ore");
        this.selectNewProductionItem();
        this.lowerProductionPossible = 8;
        this.upperProductionPossible = 64;
        this.efficiencyWithoutRequirement = 0.10f;
        this.baseSalary = 25;
    }
}
