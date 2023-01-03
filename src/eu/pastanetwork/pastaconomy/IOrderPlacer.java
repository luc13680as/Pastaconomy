package eu.pastanetwork.pastaconomy;

public interface IOrderPlacer {
    Market market;
    public void addMarket(Market theMarket);
    public boolean hasMarket()
    public boolean checkOrder(Market.Order order);
    public boolean processOrder(Market.Order order);
}
