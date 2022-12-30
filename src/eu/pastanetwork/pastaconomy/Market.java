package eu.pastanetwork.pastaconomy;

import eu.pastanetwork.pastaconomy.companies.company;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Market {
    private static final DecimalFormat df = new DecimalFormat("0.00");
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
        public BigDecimal price;

    }
    public Market(City theCity){
        this.cityOfMarket = theCity;
        this.orderList = new ArrayList<>();
        this.itemRegistry = ItemRegistry.getInstance();
    }

    public boolean placeOrder(String theTypeofOrder, Object theFrom, String theItem, int theAmount, BigDecimal thePrice){
        checkPlaceOrder(theTypeofOrder, theFrom, theItem, theAmount, thePrice);
        Order createdOrder = new Order();
        createdOrder.from = theFrom;
        createdOrder.item = theItem;
        createdOrder.amount = theAmount;
        thePrice = thePrice.setScale(2, RoundingMode.HALF_EVEN);
        createdOrder.price = thePrice;
        if (theTypeofOrder == "sell"){
            createdOrder.type = Order.TypeOrder.SELL;
        } else {
            createdOrder.type = Order.TypeOrder.BUY;
        }
        this.orderList.add(createdOrder);
        return true;
    }

    private void checkPlaceOrder(String theTypeofOrder, Object theFrom, String theItem, int theAmount, BigDecimal thePrice){
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
        if (theAmount <= 0){
            throw new IllegalArgumentException("Amount cannot be lower than 1");
        }
        if (thePrice.compareTo(new BigDecimal("0")) <= 0){
            throw new IllegalArgumentException("price cannot be lower than 1");
        }
    }

    public ArrayList<Order> getPlacedOrder(Object theFrom){
        this.checkSearchOrderFrom(theFrom);
        return this.orderList.parallelStream()
                .filter(order -> order.from.equals(theFrom))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Order> searchOrder(String typeOfOrder, String theItem){
        this.checkSearchOrderTypeOfOrder(typeOfOrder);
        this.checkSearchOrderItem(theItem);

        Predicate<Order> typeOfFilter;
        if(typeOfOrder.equals("buy")){
            typeOfFilter = o -> o.type.equals(Order.TypeOrder.BUY);
        } else{
            typeOfFilter = o -> o.type.equals(Order.TypeOrder.SELL);
        }
        return this.orderList.parallelStream()
                .filter(typeOfFilter)
                .filter(order -> order.item.equals(theItem))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Order> searchOrder(String typeOfOrder, Object theFrom, String theItem){
        this.checkSearchOrderTypeOfOrder(typeOfOrder);
        this.checkSearchOrderItem(theItem);
        this.checkSearchOrderFrom(theFrom);

        Predicate<Order> typeOfFilter;
        if(typeOfOrder.equals("buy")){
            typeOfFilter = o -> o.type.equals(Order.TypeOrder.BUY);
        } else{
            typeOfFilter = o -> o.type.equals(Order.TypeOrder.SELL);
        }
        return this.orderList.parallelStream()
                .filter(typeOfFilter)
                .filter(order -> order.item.equals(theItem))
                .filter(order -> order.from.equals(theFrom))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void checkSearchOrderTypeOfOrder(String theTypeofOrder){
        if(!(theTypeofOrder.equals("any")) && !(theTypeofOrder.equals("sell")) && !(theTypeofOrder.equals("buy"))){
            throw new IllegalArgumentException("Wrong type of order");
        }
    }
    private void checkSearchOrderItem(String theItem){
        if (!this.itemRegistry.CheckItemExist(theItem)){
            throw new IllegalArgumentException("Item doesn't exist in the item registry");
        }
    }

    private void checkSearchOrderFrom(Object obj){
        if (!(obj instanceof company) && !(obj instanceof citizen)){
            throw new IllegalArgumentException("From doesn't contain a valid object");
        }
    }

    public ArrayList<Order> displayOrders(){
        return this.orderList.stream().collect(Collectors.toCollection(ArrayList::new));
    }
}
