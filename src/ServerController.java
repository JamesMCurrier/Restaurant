import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class ServerController {
    @FXML
    private ComboBox<String> employeeComboBox;
    @FXML
    private ListView<String> currOrderList, menuItemsList, readyOrderList;
    @FXML
    private Label currOrderLabel, costLbl, menuItemsLbl;
    @FXML
    private Button createOrderBtn, sendOrderBtn, viewOrdersBtn, addItemBtn, modifyItemBtn, ServeBtn;

    private Server currServer;
    private static HashMap<String, Ingredient> ingredientStock;
    private static HashMap<String, MenuItem> menuItems;
    private static HashMap<String, Server> employees = new HashMap<>();

    /**
     * Given a list of employees, adds all type server employees to the static Map of employees.
     * @param employees List of employees of which to add.
     */
    public static void setEmployeesList(ArrayList<Employee> employees){
        for(Employee employee : employees){
            if(employee instanceof Server) {
                ServerController.employees.put(employee.getName(), (Server)employee);
            }
        }
    }

    /**
     * Given a Map of MenuItems, set them to the static menuItems.
     * @param menuItems Map of Menu Items to be set.
     */
    public static void setMenuItemsList(HashMap<String, MenuItem> menuItems){
        ServerController.menuItems = menuItems;
    }

    /**
     * Sets the static stock variable.
     * @param stock HashMap of Ingredient Name to Ingredient Object
     */
    public static void setIngredientStock(HashMap<String, Ingredient> stock){
        ServerController.ingredientStock = stock;
    }

    /**
     * Adds the names of all servers On the GUI and sets all the menu items.
     */
    public void initializeServerScene(){
        if(employeeComboBox.getItems().size() == 0) {
            for (String serverName : employees.keySet()) {
                employeeComboBox.getItems().add(serverName);
            }
            this.setMenuItemsListView();
        }
    }

    /**
     * Refreshes the list of Menu Items on the GUI
     */
    public void setMenuItemsListView() {
        menuItemsList.getItems().clear();
        menuItemsList.getItems().addAll(menuItems.keySet());
    }

    /**
     * Setter for the current servers, while also enabling buttons only available once a server is chosen.
     * @param event
     */
    public void setCurrServer(ActionEvent event){
        String serverName = ((ComboBox)event.getSource()).getSelectionModel().getSelectedItem().toString();
        currServer = employees.get(serverName);
        createOrderBtn.setDisable(false);
        viewOrdersBtn.setDisable(false);
    }

    /**
     * Creates a new order for the server.
     */
    public void createOrder() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Please input the table number for this order.");
        Optional<String> input = dialog.showAndWait();
        try {
            if (input.isPresent()) {
                int tableNum = Integer.valueOf(input.get());
                if (tableNum > 20 || tableNum < 1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please input a numeric table number between 1 and 20. ");
                    alert.show();
                    return;
                }
                currServer.createNewOrder(tableNum);
                currOrderLabel.setText("Current Order: Table " + tableNum);
                sendOrderBtn.setDisable(false);
                addItemBtn.setDisable(false);
                currOrderList.getItems().clear();
            }
        }
        catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please input a numeric table number between 1 and 20. ");
            alert.show();
        }
    }

    /**
     * Adds an item to the current order, the item being the currently selected item in the Server's GUI.
     */
    public void addToOrder(){
        try {
            if (addItemBtn.getText().equals("Add Ingredient")) {
                this.addIngredient();
                return;
            }
            MenuItem item = menuItems.get(menuItemsList.getSelectionModel().getSelectedItem());
            currServer.addToOrder(item.makeCopy());
            String itemName = menuItemsList.getSelectionModel().getSelectedItem();
            currOrderList.getItems().add(itemName);
            costLbl.setText(String.valueOf(currServer.getOrderCost()));
            modifyItemBtn.setDisable(false);
        }
        catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please Select Item.");
            alert.show();
        }
    }

    /**
     * Adds the current selected ingredient to the currently selected menuItem.
     */
    public void addIngredient(){
        String ingredientName = menuItemsList.getSelectionModel().getSelectedItem();
        Ingredient ingredient = ingredientStock.get(ingredientName);

        String menuItemName = currOrderList.getSelectionModel().getSelectedItem();
        int menuItemIndex = currOrderList.getSelectionModel().getSelectedIndex();
        MenuItem menuItem = currServer.getOrderItem(menuItemIndex);

        menuItem.addIngredient(ingredient, 1);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Added 1 "
                + ingredientName +" to the " + menuItemName);
        alert.show();

        menuItemsLbl.setText("Menu Items");
        this.setMenuItemsListView();
        addItemBtn.setText("Add Item");
    }

    /**
     * Finalizes the order, sending it to any observers.
     */
    public void SendOrder(){
        int s = currServer.getFailedOrders().size();
        sendOrderBtn.setDisable(true);
        addItemBtn.setDisable(true);
        currServer.finalizeOrder();
        currOrderList.getItems().clear();
        costLbl.setText("0");
        currOrderLabel.setText("No Current Order");
        modifyItemBtn.setDisable(true);

        if (currServer.getFailedOrders().size() > s){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "There are not enough ingredients to complete this order. ");
            alert.show();
        }
    }

    /**
     * Updates the current list of orders ready to be served.
     */
    public void viewOrders(){
        readyOrderList.getItems().clear();
        if (currServer.getReadyToServe().size() != 0) {
            readyOrderList.getItems().clear();
            for (Order order : currServer.getReadyToServe()) {
                readyOrderList.getItems().add(order.toString());
            }
        }
    }

    /**
     * Changes the menu items pane to a list of ingredients to allow for modification to the a menu item.
     */
    public void modifyItem(){
        menuItemsLbl.setText("Ingredients");
        menuItemsList.getItems().clear();
        menuItemsList.getItems().addAll(ingredientStock.keySet());
        addItemBtn.setText("Add Ingredient");
    }

    /**
     * Serves the currently selected order.
     */
    public void serveOrder(){
        int orderIndex = readyOrderList.getSelectionModel().getSelectedIndex();
        if(orderIndex == -1){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please select the order to serve.");
            alert.show();
            return;
        }
        Order order = currServer.getReadyToServe().get(orderIndex);
        currServer.serveOrder(order);
        readyOrderList.getItems().remove(orderIndex);
    }
}
