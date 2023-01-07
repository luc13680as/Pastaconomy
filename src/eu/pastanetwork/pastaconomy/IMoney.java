package eu.pastanetwork.pastaconomy;

import java.math.BigDecimal;

public interface IMoney {
    public BigDecimal GetMoney();
    public void ReceiveMoney(BigDecimal moneyReceived);
    public boolean SpendMoney(BigDecimal moneyToSpend);
}
