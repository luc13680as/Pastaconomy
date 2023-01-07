package eu.pastanetwork.pastaconomy;

import eu.pastanetwork.pastaconomy.companies.AgricultureCompany;
import eu.pastanetwork.pastaconomy.companies.MiningCompany;
import eu.pastanetwork.pastaconomy.companies.company;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
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
        public BigDecimal price;

        public BigDecimal getPrice(){
            return this.price;
        }
        public String getItem(){
            return this.item;
        }
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

    public void indexOrdersPerOrder(){
        Comparator<Order> comparatorPerPrice = Comparator.comparing(Order -> Order.price);
        List <BigDecimal> theSorted = this.orderList.parallelStream()
                .filter(o -> o.type.equals(Order.TypeOrder.BUY))
                .filter(order -> order.item.equals("Fish"))
                .sorted(comparatorPerPrice.reversed())
                .map(Order::getPrice)
                .collect(Collectors.toList());

        System.out.println(theSorted);
    }

    public void matchOrders(){
        ArrayList<Order> tempOrderList = this.orderList;

        Set<String> itemTypes = tempOrderList.parallelStream()
                .map(Order::getItem)
                .collect(Collectors.toSet());

        Comparator<Order> comparatorPerPrice = Comparator.comparing(Order -> Order.price);
        for(String theItem : itemTypes){
            List<Order> buyOrders = tempOrderList.parallelStream()
                    .filter(o -> o.type.equals(Order.TypeOrder.BUY))
                    .filter(o -> o.item.equals(theItem))
                    .sorted(comparatorPerPrice.reversed())
                    .collect(Collectors.toList());

            List<Order> sellOrders = tempOrderList.parallelStream()
                    .filter(o -> o.type.equals(Order.TypeOrder.SELL))
                    .filter(o -> o.item.equals(theItem))
                    .sorted(comparatorPerPrice)
                    .collect(Collectors.toList());

            if((sellOrders.size() == 0) || (buyOrders.size() == 0)){
                continue;
            }
            loopThroughOrders(buyOrders, sellOrders);
        }
    }

    private void loopThroughOrders(List<Order> providedBuyOrders, List<Order> providedSellOrders){
        for(Order theBuyOrder : providedBuyOrders){
            for (Order theSellOrder : providedSellOrders){
                if (theBuyOrder.price.compareTo(theSellOrder.price) >= 0){
                    executeOrder(theBuyOrder, theSellOrder);
                } else {
                    return;
                }
            }
        }
    }

    private void executeOrder(Order buyOrder, Order sellOrder){
        if (!(buyOrder.price.compareTo(sellOrder.price) >= 0)) {
            //Debug
            System.out.println("Not matching price detected");
            return;
        }
        if (!(buyOrder.type.equals(Order.TypeOrder.BUY)) || !(sellOrder.type.equals(Order.TypeOrder.SELL))){
            throw new IllegalArgumentException("Wrong type of order provided");
        }

        buyOrder.to = sellOrder.from;
        sellOrder.to = buyOrder.from;
        if(buyOrder.amount >= sellOrder.amount){
            int amountExecuted = sellOrder.amount;
            int remaining = buyOrder.amount - amountExecuted;
            //this.removeorder(sellOrder);
            //this.removeorder(buyOrder);
            //this.addOrder(buyOrdermodified);
        } else {
            int amountExecuted = buyOrder.amount;
            int remaining = sellOrder.amount - amountExecuted;
        }

        return;
    }
}
