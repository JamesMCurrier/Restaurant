import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents a chef employee. This class extends the Employee class. A Chef can fulfill orders.
 */
public class Chef extends Employee implements Receiver{
    private ArrayList<Order> ordersSeen = new ArrayList<>();
    private ArrayList<Order> ordersWaiting = new ArrayList<>();
    private ArrayList<Order> ordersReady = new ArrayList<>();

    /**
     * Constructor for the Chef class. Create a new Chef with specified name.
     * @param name The name.
     */
    public Chef(String name, Menu ourMenu, Inventory inventory) {
        super(name, ourMenu, inventory);
    }

    private void orderEvent(Event event) {
        ordersWaiting.add(event.getOrder());
    }

    public void handleEvent(Event event){
        if (event.getEventType().equals(EventType.ORDER)){
            orderEvent(event);
        }
        if (event.getEventType().equals(EventType.REMOVE_ORDER)){
            for (int i = 0; i < ordersWaiting.size(); i++) {
                if ((ordersWaiting.get(i).getId() == event.getOrder().getId())&& !(event.getExp().equals(getName()))) {
                    ordersWaiting.remove(i);
                    break;
                }
            }
        }
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
     * Confirm the chef has seen order.
     * @param order The order that the chef has seen.
     */
    public void confirmSeeOrder(Order order){
        ordersSeen.add(order);
    }

    /**
     * Whenever the Chef completes an order, this is called.
     * @param order The order that is ready.
     */
    public void orderReady(Order order){
        Event e = new Event(EventType.ORDER_READY);
        e.addOrder(order);
        e.setServer(order.getServerName());
        sendEvent(e);

        Event b = new Event(EventType.REMOVE_ORDER);
        b.addOrder(order);
        b.setExp("");
        sendEvent(b);
    }


    /**
     * Whenever a finished order is brought out of the kitchen, this is called.
     * @param order The order that has been taken.
     */
    public void orderDelivered(Order order){
        ordersReady.remove(order);
    }

    public ArrayList<Order> getOrdersWaiting() {
        return ordersWaiting;
    }

    public ArrayList<Order> getOrdersReady() {
        return ordersReady;
    }

    public Order getOrderWaiting(int id){
        for (Order i : ordersWaiting){
            if (i.getId() == id){
                return i;
            }
        }
        return null;
    }
}
