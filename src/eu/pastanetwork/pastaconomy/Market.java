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
    ArrayList<Order> ordersToRemove;
    City cityOfMarket;
    ItemRegistry itemRegistry;

    public static class Order{
        public enum TypeOrder{
            SELL, BUY
        }
        public TypeOrder type;
        public IOrderPlacer from;
        public IOrderPlacer to;
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
        this.ordersToRemove = new ArrayList<>();
        this.itemRegistry = ItemRegistry.getInstance();
    }

    public boolean placeOrder(String theTypeofOrder, IOrderPlacer theFrom, String theItem, int theAmount, BigDecimal thePrice){
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

    private void checkPlaceOrder(String theTypeofOrder, IOrderPlacer theFrom, String theItem, int theAmount, BigDecimal thePrice){
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

    public ArrayList<Order> getPlacedOrder(IOrderPlacer theFrom){
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

    public ArrayList<Order> searchOrder(String typeOfOrder, IOrderPlacer theFrom, String theItem){
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

    private void checkSearchOrderFrom(IOrderPlacer obj){
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
        deleteOrders();
    }

    private void loopThroughOrders(List<Order> providedBuyOrders, List<Order> providedSellOrders){
        buyloop:
        for(Order theBuyOrder : providedBuyOrders){
            for (Order theSellOrder : providedSellOrders){
                if (!(theBuyOrder.amount > 0) || !(theBuyOrder.to == null)){
                    continue buyloop;
                }
                if (!(theSellOrder.amount > 0) || !(theSellOrder.to == null)){
                    continue;
                }
                if ((theBuyOrder.price.compareTo(theSellOrder.price) >= 0)){
                    executeOrder(theBuyOrder, theSellOrder);
                }
            }
        }
    }

    private void executeOrder(Order buyOrder, Order sellOrder){
        int amountExecuted = 0;
        int remainingToBuy = 0;
        int remainingToSell = 0;
        if(buyOrder.amount > sellOrder.amount){
            amountExecuted = sellOrder.amount;
            remainingToBuy = buyOrder.amount - amountExecuted;
        } else if (buyOrder.amount < sellOrder.amount) {
            amountExecuted = buyOrder.amount;
            remainingToSell = sellOrder.amount - amountExecuted;
        } else{
            amountExecuted = sellOrder.amount;
        }
        buyOrder.from.processOrder(buyOrder, amountExecuted, sellOrder.price);
        sellOrder.from.processOrder(sellOrder, amountExecuted, sellOrder.price);
        //buyOrder.to = sellOrder.from;
        //sellOrder.to = buyOrder.from;
        //this.ordersToRemove.add(buyOrder);
        //this.ordersToRemove.add(sellOrder);

        /*if(remainingToBuy != -1){
            //this.placeOrder("buy", buyOrder.from, buyOrder.item, remainingToBuy, buyOrder.price);
        } else if (remainingToSell != -1){
            //this.placeOrder("sell", sellOrder.from, sellOrder.item, remainingToSell, sellOrder.price);
        } */
        if (remainingToBuy == 0){
            buyOrder.to = sellOrder.from;
            this.ordersToRemove.add(buyOrder);
        }
        buyOrder.amount = remainingToBuy;
        if (remainingToSell == 0){
            sellOrder.to = buyOrder.from;
            this.ordersToRemove.add(sellOrder);
        }
        sellOrder.amount = remainingToSell;

        System.out.println("[TRANSACTION] " + buyOrder.item + " " + amountExecuted + " units at " + sellOrder.price.stripTrailingZeros().toPlainString() + "€");
    }

    private void deleteOrders(){
        this.orderList.removeAll(this.ordersToRemove);
        this.ordersToRemove.clear();
    }
}
