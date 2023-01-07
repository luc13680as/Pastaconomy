package eu.pastanetwork.pastaconomy;

public interface IOrderPlacer {
    public void addMarket(Market theMarket);
    public boolean hasMarket();
    public boolean processOrder(Market.Order order, int quantity);
}
