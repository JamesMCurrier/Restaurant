import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a Restaurant. It has Employees, Bills, an Inventory, and a Menu.
 */
public class Restaurant extends Application implements EventReceiver {
    // The file to read Employees in from.
    final private static String EMPLOYEE_FILE = "employees.txt";

    // The number of Tables in the Restaurant.
    private int numTables = 20;

    // Contains the list of all employees currently working according to the configuration file.
    ArrayList<Employee> employees = new ArrayList<>(0);

    Inventory inventory;
    // Contains the Menu that is going to be loaded from a configuration file.
    Menu ourMenu;
    // There are a total of 20 bills in our restaurant because it only has 20 tables.
    Bill[] bills;

    private EventLogger eventLogger;

    /**
     * Constructor for the class Restaurant. It performs the Employee setup by calling setupEmployees, loading the employees from a configuration
     * file and it also initializes the Bills by calling newBills.
     */
    public Restaurant() {
        deserializeInventory();
        deserializeMenu();
        setupEmployees();
        newBills();
        setupControllers();
        eventLogger = new EventLogger();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("EmployeeView.fxml"));
        primaryStage.setTitle("Restaurant");
        primaryStage.setScene(new Scene(root, 600, 420));
        primaryStage.show();
    }


    /**
     * Deserialize the Inventory
     */
    private void deserializeInventory(){
        try{
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("Inventory.txt"));
            this.inventory = (Inventory) in.readObject();
            in.close();
        }
        catch(Exception e){
            this.inventory = new Inventory();
            e.printStackTrace();
        }
    }

    /**
     * Deserialize the Menu
     */
    private void deserializeMenu(){
        try{
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("MenuSerialize.txt"));
            this.ourMenu = (Menu) in.readObject();
            in.close();
        }
        catch(Exception e){
            this.ourMenu = new Menu(inventory);
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws IOException {
        FileOutputStream fout = new FileOutputStream("Inventory.txt");
        ObjectOutputStream out = new ObjectOutputStream(fout);
        out.writeObject(inventory);
        out.flush();

        fout = new FileOutputStream("MenuSerialize.txt");
        out = new ObjectOutputStream(fout);
        out.writeObject(ourMenu);
        out.flush();
    }


    @Override
    public void handleEvent(Event event) {
        String message = event.getEventType().toString();
        eventLogger.logInfo(message);
        // Send Event to all Employees.
        for (Employee employee : this.getEmployees()) {
            employee.handleEvent(event);
        }
        if (event.getEventType() == EventType.ORDER){
            bills[event.getOrder().getTableNumber() - 1].addToBill(event.getOrder());
        }
    }

    /**
     * Function to initialize the Bills when the Restaurant opens. This initializes 20 Bills because our restaurant
     * only has 20 tables.
     */
    private void newBills(){
        bills = new Bill[numTables];
        for(int i = 0; i < numTables; i++){
            bills[i] = new Bill(i + 1);
        }
    }


    /**
     * Setups the controllers for the various GUI's in the restaurant
     */
    public void setupControllers(){
        ChefController.setEmployeesList(employees);
        ServerController.setEmployeesList(employees);
        ReceiverController.setEmployeesList(employees);
        ServerController.setMenuItemsList(ourMenu.getMenuItems());
        ServerController.setIngredientStock(inventory.getIngredients());
        TableController.loadBills(bills);
        ManagerController.setEmployeesList(employees);
        ManagerController.setMenuItemsList(ourMenu.getMenuItems());
        ManagerController.setIngredientList(inventory.getStock());
    }

    /**
     * Set up and store the Employees stored in the configuration file.
     */
    private void setupEmployees() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(EMPLOYEE_FILE));
            String line = reader.readLine();
            while (line != null) {
                String[] data = line.split(",");
                String name = data[0].trim();
                String type = data[1].trim();

                // Determine the correct type and create the Employee accordingly.
                if (type.equals("Manager")) {
                    this.employees.add(new Manager(name, this.ourMenu, this.inventory));
                } else if (type.equals("Chef")) {
                    this.employees.add(new Chef(name, this.ourMenu, this.inventory));
                } else if (type.equals("Server")) {
                    this.employees.add(new Server(name, this.ourMenu, this.inventory));
                }
                // Move on to the next line.
                line = reader.readLine();
            }
            reader.close();

            for(Employee employee: this.employees){
                employee.addListener(this);
            }
        } catch (IOException e) {
            System.out.println("Something has gone wrong while attempting to read from the file.");
            e.printStackTrace();
        }
    }

    /**
     * Returns a List containing all of the Employees working in this Restaurant.
     * @return The List of Employees.
     */
    public List<Employee> getEmployees() {
        return this.employees;
    }

}
