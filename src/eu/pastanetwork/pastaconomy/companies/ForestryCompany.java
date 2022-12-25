package eu.pastanetwork.pastaconomy.companies;
import eu.pastanetwork.pastaconomy.companies.company;
import eu.pastanetwork.pastaconomy.citizen;

import java.util.ArrayList;
import java.util.Random;

public class ForestryCompany extends company{
    public ForestryCompany(citizen creator){
        super(creator);
        //this.companySector = "Primary";
        this.productionToolRequirement = "Axe";
        this.possibleProductionList = new ArrayList<>();
        this.possibleProductionList.add("Wood");
        this.selectNewProductionItem();
        this.lowerProductionPossible = 16;
        this.upperProductionPossible = 64;
        this.efficiencyWithoutRequirement = 0.10f;
        this.baseSalary = 50;
    }
}
