import java.util.ArrayList;

/**
 * The Order class represents an Order that a Server can make. Each order contains a unique id, a list of all menu items
 * that make up the order, and the table number that the order is for.
 */
public class Order {
    // Used to generate a unique id for each order.
    private static int nextId = 0;
    private String serverName;
    private int id;
    private ArrayList<MenuItem> menuItems;
    private boolean[] payed;
    private int tableNum;
    private int cost;
    /**
     * Create a new Order with the indicated table number.
     * @param tableNum The table number relating to this order.
     */
    public Order(int tableNum) {
        this.payed = new boolean[100];
        this.tableNum = tableNum;
        this.id = Order.nextId++;
        this.cost = 0;
        this.menuItems = new ArrayList<>();
    }

    /**
     * Add a new item to this order.
     * @param item The MenuItem to add.
     */
    public void addToOrder(MenuItem item) {
        this.cost += item.getCost();
        this.menuItems.add(item);
    }

    /**
     * Getter for the ammount of MenuItems on this Order.
     * @return Size.
     */
    public int getSize(){
        return menuItems.size();
    }

    /**
     * Getter for the information about the MenuItems contained here. It includes cost and name.
     * @return ArrayList of Strings with information about each MenuItem.
     */
    public ArrayList<String> getItemsNames(){
        ArrayList<String> items = new ArrayList<>();
        for(MenuItem i : menuItems){
            if(!i.getPayed())items.add("Item: " +i.getName() + ". Cost: " + i.getCost() + " CAD");
        }
        return items;
    }

    /**
     * Getter for the list of MenuItems that were not payed yet.
     * @return ArrayList of MenuItem.
     */
    public ArrayList<MenuItem> getUnpaidItems(){
        ArrayList<MenuItem> temp = new ArrayList<>();
        for(MenuItem i : menuItems){
            if(!i.getPayed())temp.add(i);
        }
        return temp;
    }

    /**
     * Getter for the list of menuItems
     * @return list of MenuItems
     */
    public ArrayList<MenuItem> getItems(){
        return menuItems;
    }


    /**
     * Get the table number that this order is for.
     * @return The table number.
     */
    public int getTableNumber() {
        return this.tableNum;
    }

    /**
     * Returns the id of this order.
     * @return The id.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Getter for the cost of the Order.
     * @return Cost of the Order.
     */
    public int getCost(){
        return this.cost;
    }

    /**
     * toString Function to print the orders, it includes the order number and the items in it, besides the total cost.
     * @return String to represent visually an order.
     */
    @Override
    public String toString(){
        String ret = "Order number: " + id + ", Table number: " + tableNum + "\n";
        for(MenuItem it : menuItems){
            ret += it + "\t\t+" + it.getCost() + "\n";
        }
        ret += "Total of this order: \t +" + cost;
        return ret;
    }

    /**
     * Setter for the Server's name.
     * @param name Server's name.
     */
    public void setServerName(String name){
        serverName = name;
    }

    /**
     * Getter for the Server's name.
     * @return String Server's name.
     */
    public String getServerName(){
        return serverName;
    }
}

