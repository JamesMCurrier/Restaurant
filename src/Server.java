import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents a server employee. This class extends the Employee class. A Server can create orders,
 * deliver food, etc.
 */
public class Server extends Employee implements Receiver {
    private Order currOrder;
    private ArrayList<Order> readyToServe = new ArrayList<>();
    private ArrayList<Order> failedOrders = new ArrayList<>();

    /**
     * Constructor for Server. Create a new Server with the provided name.
     * @param name The name of the Server.
     */
    public Server(String name, Menu ourMenu, Inventory inventory) {
        super(name, ourMenu, inventory);
    }

    /**
     * Creates a new order for the specified table.
     * @param tableNum The table number of the new order.
     */
    public void createNewOrder(int tableNum){
        currOrder = new Order(tableNum);
        currOrder.setServerName(this.getName());
    }

    /**
     * Add a menu item to current order .
     * @param item The menu item to be added to the current order.
     */
    public void addToOrder(MenuItem item){
        currOrder.addToOrder(item);
    }

    /**
     * Returns the <index> item of the curr order
     * @param index the position of the item to be returned in the list
     * @return the item in the list at index <index>
     */
    public MenuItem getOrderItem(int index){
        return currOrder.getItems().get(index);
    }

    /**
     * Completes the order and notifies the appropriate recipients that a new order has been ordered.
     */
    public void finalizeOrder(){
        if (canMake(currOrder)) {
            Event orderEvent = new Event(EventType.ORDER);
            orderEvent.addOrder(currOrder);
            orderEvent.setServer(this.getName());
            this.sendEvent(orderEvent);

            for (MenuItem MI : currOrder.getUnpaidItems()) {
                for (Ingredient ingredient : MI.getIngredients().keySet()) {
                    ingredient.removeStock(MI.getIngredients().get(ingredient));
                }
            }
        }
        else {
            Event e = new Event(EventType.UNABLE_TO_COMPLETE);
            e.addOrder(currOrder);
            e.setServer(this.getName());
            sendEvent(e);
        }
    }

    /**
     * Verifies the current order can be make with the ingredients in stock.
     * @param order order to check
     * @return true iff the order can be made
     */
    private boolean canMake(Order order){
        MenuItem all = new MenuItem("all", 0, new HashMap<>());

        for (MenuItem MI : order.getUnpaidItems()){
            for (Ingredient ingredient : MI.getIngredients().keySet()){
                all.addIngredient(ingredient, MI.getIngredients().get(ingredient));
            }
        }

        for (Ingredient ing : all.getIngredients().keySet()){
            if (all.getIngredients().get(ing) > ing.getAmount()){
                return false;
            }
        }
        return true;
    }


    /**
     * Restocks the given set of ingredients and their quantities to the inventory.
     * @param map Map of the ingredients to be restocked in <String, Integer> form,
     *           where the String key value is the name of the ingredient.
     */
    public void addToInventory(Map<String, Integer> map){
        for(Map.Entry<String, Integer> entry : map.entrySet()){
            String ingredientName = entry.getKey();
            Integer quantity = entry.getValue();
            inventory.addIngredient(ingredientName, quantity);
        }
    }


    /**
     * Servers the order.
     * @param order order to be served
     */
    public void serveOrder(Order order){
        readyToServe.remove(order);
        Event serveEvent = new Event(EventType.SERVE);
        serveEvent.addOrder(currOrder);
        serveEvent.setServer(this.getName());
        this.sendEvent(serveEvent);
    }

    /**
     * Returns a list of failed orders due to insufficient ingredients.
     * @return arraylist of failed orders
     */
    public ArrayList<Order> getFailedOrders(){
        return failedOrders;
    }

    /**
     * Returns a list of orders ready to be served
     * @return List of orders ready to be served
     */
    public ArrayList<Order> getReadyToServe(){
        return readyToServe;
    }

    /**
     * Getter for the current orders cost
     * @return current order cost or -1
     */
    public int getOrderCost(){
        if(currOrder == null) return -1;
        return currOrder.getCost();
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventType().equals(EventType.ORDER_READY)){
            if(event.getServer().equals(this.getName())){
                readyToServe.add(event.getOrder());
            }
        }
        if (event.getEventType().equals(EventType.UNABLE_TO_COMPLETE)){
            failedOrders.add(event.getOrder());
        }
    }
}

