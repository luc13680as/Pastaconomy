package eu.pastanetwork.pastaconomy;

import java.math.BigDecimal;

public interface IOrderPlacer {
    public void addMarket(Market theMarket);
    public boolean hasMarket();
    public boolean checkOrder(Market.Order theOrder, int quantity);
    public boolean processOrder(Market.Order order, int quantity, BigDecimal price);
}
