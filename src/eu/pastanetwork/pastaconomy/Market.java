package eu.pastanetwork.pastaconomy;

import eu.pastanetwork.pastaconomy.companies.company;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Market {
    ArrayList<Order> orderList;
    City cityOfMarket;
    ItemRegistry itemRegistry;
    public static class Order{
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
        this.orderList = new ArrayList<>();
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
        } else {
            createdOrder.type = Order.TypeOrder.BUY;
        }
        this.orderList.add(createdOrder);
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

    public ArrayList<Order> searchOrder(String typeOfOrder, String theItem){
        if(!(typeOfOrder.equals("any")) && !(typeOfOrder.equals("sell")) && !(typeOfOrder.equals("buy"))){
            throw new IllegalArgumentException("Wrong type of order");
        }
        if (!this.itemRegistry.CheckItemExist(theItem)){
            throw new IllegalArgumentException("Item doesn't exist in the item registry");
        }

        Predicate<Order> typeOfFilter;
        if(typeOfOrder.equals("buy")){
            typeOfFilter = o -> o.type.equals(Order.TypeOrder.BUY);
            //stream.filter(order -> order.type.equals(Order.TypeOrder.BUY));
        } else{
            typeOfFilter = o -> o.type.equals(Order.TypeOrder.SELL);
            //stream.filter(order -> order.type.equals(Order.TypeOrder.SELL));
        }
        return this.orderList.parallelStream()
                .filter(typeOfFilter)
                .filter(order -> order.item.equals(theItem))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
