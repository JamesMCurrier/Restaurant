import java.util.ArrayList;

/**
 * The Employee class represents a specific Employee. It stores it's name and unique id.
 *
 * An Employee should never be instantiated directly. Rather, one should instead instantiate one of the subclasses.
 * Each of the subclasses represents a specific type of Employee.
 *
 * Employee implements EventCreator interface. As a result, it is able to send Events to all EventReceivers that are
 * watching/listening.
 */
public abstract class Employee implements EventCreator, EventReceiver {
    private ArrayList<EventReceiver> observers = new ArrayList<>();
    private String name;
    // Static field that is used to generate a unique id whenever new instance of Employee class is created.
    private static int nextId;
    private int id;
    Menu ourMenu;
    Inventory inventory;

    /**
     * Constructor for Employee class. Note that Employees should not be instantiated directly - instead instantiate
     * one of the subclasses.
     * @param name The name of the Employee.
     * @param ourMenu The Restaurant's menu.
     * @param inventory The Restaurant's inventory.
     */
    public Employee(String name, Menu ourMenu, Inventory inventory) {
        this.name = name;
        // Assign and then increment.
        this.id = Employee.nextId ++;
        this.ourMenu = ourMenu;
        this.inventory = inventory;
    }

    @Override
    public void addListener(EventReceiver eventReceiver) {
        this.observers.add(eventReceiver);
    }

    @Override
    public void removeListener(EventReceiver eventReceiver) {
        this.observers.remove(eventReceiver);
    }

    @Override
    public void sendEvent(Event event) {
        for (EventReceiver observer : this.observers) {
            observer.handleEvent(event);
        }
    }

    /**
     * Returns the name of the Employee.
     * @return The name of the Employee.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Change the name of this Employee.
     * @param newName The new name of this Employee.
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Returns the id of this Employee.
     * @return The id of this Employee.
     */
    public int getId(){
        return this.id;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
