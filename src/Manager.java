//import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.ArrayList;
import java.util.Map;

/**
 * Class that represents a manager. This class extends the Employee class. The Manager controls how the Restuarant is
 * run. Managers are able to order new supplies to restock the Inventory, among other things.
 */
public class Manager extends Employee implements Receiver {

    // The list of orders that are currently in the system that have not been delivered.
    private ArrayList<Order> ordersActive = new ArrayList<>();

    /**
     * Create a new Manager with the specified name.
     * @param name The name of the Manager.
     */
    public Manager(String name, Menu ourMenu, Inventory inventory) {
        super(name, ourMenu, inventory);
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
     * Returns the Inventory stored by this Manager.
     * @return The Inventory.
     */
    public Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Change the specified Ingredient's request threshold: the minimum amount that of the Ingredient in the inventory
     * before more of the Ingredient has to be ordered in the resupply.
     * @param ingredientName The name of the Ingredient to change the request threshold.
     * @param newThreshold The new threshold before resupply is needed for the Ingredient.
     */
    private void changeRequestThreshold(String ingredientName, int newThreshold) {
        this.inventory.changeRequestThreshold(ingredientName, newThreshold);
    }

    /**
     * Change the specified Ingredient's amount to request when ordering/restocking supplies.
     * @param ingredientName The name of the Ingredient to change the request amount.
     * @param newRequestAmount The new request amount.
     */
    public void changeRequestAmount(String ingredientName, int newRequestAmount) {
        this.inventory.changeRequestAmount(ingredientName, newRequestAmount);
    }

    /**
     * Show a list of all the Ingredients of the Inventory to the screen.
     */
    public void printInventory() {
        System.out.println(this.inventory);
    }

    /**
     * Returns an ArrayList containing all of the active Orders in the System.
     * @return The ArrayList of active Orders.
     */
    public ArrayList<Order> getActiveOrders() {
        return this.ordersActive;
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventType().equals("print inventory")) {
            this.printInventory();
        }

        // A new Order has been added to the system.
        if (event.getEventType().equals(EventType.ORDER)) {
            this.ordersActive.add(event.getOrder());
        }

        // An order has been removed from the system.
        if (event.getEventType().equals(EventType.REMOVE_ORDER)) {
            this.ordersActive.remove(event.getOrder());
        }
    }
}