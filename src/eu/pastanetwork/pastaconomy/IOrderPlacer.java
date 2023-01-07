package eu.pastanetwork.pastaconomy;

public interface IOrderPlacer {
    public void addMarket(Market theMarket);
    public boolean hasMarket();
    public void processOrder(Market.Order order);
}
