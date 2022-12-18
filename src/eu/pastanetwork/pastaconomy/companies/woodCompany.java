package eu.pastanetwork.pastaconomy.companies;
import eu.pastanetwork.pastaconomy.citizen;
import java.lang.*;

public class woodCompany extends company{
    public woodCompany(citizen creator){
        super(creator);
    }

    public void produce(){
        System.out.println("Wood was produced");
    }
}
