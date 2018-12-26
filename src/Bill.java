import java.util.ArrayList;

/**
 * The Bill class represents a Bill that stores a list of Orders. This class will also have the total value of the bill
 * and allow the Server to add and remove Orders from this Bill. Each Bill represents one table and thus have a unique
 * id.
 */

public class  Bill {
    // The unique identifier of this Bill.
    private int id;
    // This is the counter that will ensure that we have unique ids.
    private static int nextId = 0;
    // Number of the table this Bill refers to.
    private int tableNum;
    // The list of orders that the Bill contains.
    private ArrayList<Order> orders;
    // The total cost of all the orders together.
    private int cost;

    /**
     * Constructor that initializes a Bill with a table number.
     * @param tableNum This Bill's table number.
     */
    public Bill(int tableNum){
        this.id = nextId++;
        this.tableNum = tableNum;
        this.orders = new ArrayList<>();
    }

    /**
     * Adds a new order to this Bill.
     * @param order Order to be added.
     */
    public void addToBill(Order order){
        this.orders.add(order);
        this.cost += order.getCost();
    }

    /**
     * Getter for the Orders contained in this Bill.
     * @return ArrayList of Orders.
     */
    public ArrayList<Order> getOrders(){
        return this.orders;
    }

    /**
     * Getter for the table number.
     * @return The table number.
     */
    public int getTableNum(){
        return this.tableNum;
    }

    /**
     * Getter for the Id of the Bill.
     * @return Id of the Bill.
     */
    public int getId(){
        return this.id;
    }

    /**
     * Getter for the cost of the Bill.
     * @return Cost of the Bill.
     */
    public int getCost(){
        return this.cost;
    }

    /**
     * Getter for the names and price in String form of the MenuItems contained in this Bill.
     * @return ArrayList of Strings with name and price of MenuItems.
     */
    public ArrayList<String> getItemsNames(){
        ArrayList<String> items = new ArrayList<>();
        for(Order order: orders){
            items.addAll(order.getItemsNames());
        }
        return items;
    }

    /**
     * Getter for the MenuItems contained in this Bill.
     * @return ArrayList of MenuItems contained here.
     */
    public ArrayList<MenuItem> getItems(){
        ArrayList<MenuItem> items = new ArrayList<>();
        for(Order order: orders){
            items.addAll(order.getUnpaidItems());
        }
        return items;
    }

    /**
     * toString method to print a Bill. This will show the bill id, table number and all the orders in it.
     * @return String as a visual representation of the Bill.
     */
    @Override
    public String toString(){
        String ret= "Bill id: " + this.id + " ----- Table number: " + this.tableNum + "\n";
        System.out.println("Bill " +  this.id + " has " + orders.size());
        for(Order it : orders){
            ret += it + "\n";
        }
        ret += "Total of this bill: +" + this.cost;
        return ret;
    }


}
