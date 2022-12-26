package eu.pastanetwork.pastaconomy;

import eu.pastanetwork.pastaconomy.companies.company;

import java.util.ArrayList;

public class Market {
    ArrayList<Order> buyOrder;
    ArrayList<Order> sellOrder;
    City cityOfMarket;
    ItemRegistry itemRegistry;
    private static class Order{
        public enum TypeOrder{
            SELL, BUY
        }
        public TypeOrder type;
        public Object from;
        public Object to;
        public String item;
        public int amount;
        public double price;

    }
    public Market(City theCity){
        this.cityOfMarket = theCity;
        this.buyOrder = new ArrayList<>();
        this.sellOrder = new ArrayList<>();
        this.itemRegistry = ItemRegistry.getInstance();
    }

    public boolean placeOrder(String theTypeofOrder, Object theFrom, String theItem, int theAmount, double thePrice){
        checkOrder(theTypeofOrder, theFrom, theItem, theAmount, thePrice);
        Order createdOrder = new Order();
        createdOrder.from = theFrom;
        createdOrder.item = theItem;
        createdOrder.amount = theAmount;
        createdOrder.price = thePrice;
        if (theTypeofOrder == "sell"){
            createdOrder.type = Order.TypeOrder.SELL;
            this.buyOrder.add(createdOrder);
        } else {
            createdOrder.type = Order.TypeOrder.BUY;
            this.sellOrder.add(createdOrder);
        }
        return true;
    }

    private void checkOrder(String theTypeofOrder, Object theFrom, String theItem, int theAmount, double thePrice){
        if (!theTypeofOrder.equals("sell") && !theTypeofOrder.equals("buy")){
            throw new IllegalArgumentException("Wrong type of order");
        }
        if (!(theFrom instanceof citizen) && !(theFrom instanceof company)) {
            throw new IllegalArgumentException("Only a company or a citizen can place an order");
        }
        if (theTypeofOrder.equals("sell") && theFrom instanceof citizen){
            throw new IllegalArgumentException("A citizen cannot place a sell order");
        }
        if (!this.itemRegistry.CheckItemExist(theItem)){
            throw new IllegalArgumentException("Item doesn't exist in the item registry");
        }
        if ((theAmount <= 0) || (thePrice <= 0)){
            throw new IllegalArgumentException("Amount or price cannot be lower than 0");
        }
    }
}
