import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Optional;

/**
 * This is the class that controls the TableView. It has all the functionalities that were needed, such as display items and pay them.
 */
public class TableController {
    // List of orders for a specific table.
    @FXML
    private ListView<String> ordersList;
    // List of currently selected orders.
    @FXML
    private ListView<String> currentList;

    // Total price of selected items.
    private int total=0;
    // An instance of the Bill.
    private static Bill bills[];
    // Current tableNumber that was selected.
    private int tableNumber;
    // Lists containing the MenuItems of that table, and the ones that are currently selected respectively.
    private ArrayList<MenuItem> itemsList, currentItems;

    /**
     * This function loads the bills from the Restaurant. It is just a reference to the same bills.
     * @param bills Of the Restaurant.
     */
    public static void loadBills(Bill bills[]){
        TableController.bills=bills;
    }

    /**
     * This function receives a String in a determined format and from that it gets and sets the tableNumber.
     * @param table String table.
     */
    private void setTable(String table){
        if(table.charAt((table.length())-2)=='n') tableNumber = Integer.parseInt(table.charAt((table.length())-1) + "")-1;
        else tableNumber = Integer.parseInt(table.charAt((table.length())-2) + "")*10 + Integer.parseInt(table.charAt((table.length())-1) + "")-1;
    }

    /**
     * This function is called when a table is clicked. It resets the lists and reloads the ones that need to be reloaded.
     * @param event Event that was which table was pressed.
     */
    public void displayItems(ActionEvent event) {
        setTable(((Button) event.getSource()).getId());
        ordersList.getItems().clear();
        currentList.getItems().clear();
        ordersList.getItems().addAll(bills[tableNumber].getItemsNames());
        itemsList = bills[tableNumber].getItems();
        currentItems = new ArrayList<>();
    }

    /**
     * From the predetermined String it gets the Cost of the item and returns it.
     * @param item String with info about the MenuItem.
     * @return Cost of that MenuItem.
     */
    private int calculateCost(String item){
        String cost="";
        for(int i=1; i<item.length(); i++){
            if(item.charAt(i)==':' && item.charAt(i-1)=='t'){
                i+=2;
                while(item.charAt(i)!=' ') {
                    cost += item.charAt(i++);
                }
            }
        }
        return Integer.parseInt(cost);
    }

    /**
     * From the predetermined String it gets the Name of the Item and returns it.
     * @param item String with info about the MenuItem.
     * @return Name of that MenuItem.
     */
    private String findName(String item){
        String name="";
        for(int i=1; i<item.length(); i++){
            if(item.charAt(i)==':' && item.charAt(i-1)=='m'){
                i+=2;
                while(item.charAt(i)!='.'){
                    name+=item.charAt(i++);
                }
            }
        }
        return name;
    }

    /**
     * When I click to select a MenuItem, it shifts the Item to the right bar and changes all the needed information
     * such as the new cost and updates the lists of items.
     */
    public void addToList(){
        if(ordersList.getSelectionModel().getSelectedItem()==null)return;
        String item = ordersList.getSelectionModel().getSelectedItem();
        ordersList.getItems().remove(ordersList.getSelectionModel().getSelectedItem());
        currentList.getItems().addAll(item);
        total+=calculateCost(item);
        for(MenuItem i: itemsList){
            if(i.getName().equals(findName(item))){
                currentItems.add(i);
                itemsList.remove(i);
                return;
            }
        }
    }
    /**
     * When I click to unselect a MenuItem, it shifts the Item to the left bar and changes all the needed information
     * such as the new cost and updates the lists of items.
     */
    public void unselectItem(){
        if(currentList.getSelectionModel().getSelectedItem()==null)return;
        String item = currentList.getSelectionModel().getSelectedItem();
        currentList.getItems().remove(currentList.getSelectionModel().getSelectedItem());
        ordersList.getItems().addAll(item);
        total-=calculateCost(item);
        for(MenuItem i: currentItems){
            if(i.getName().equals(findName(item))){
                currentItems.remove(i);
                itemsList.add(i);
                return;
            }
        }
    }

    /**
     * Cancel an Item. It will remove the item from that order and from the screen.
     */
    public void cancelItem(){
        if(ordersList.getSelectionModel().getSelectedItem()==null)return;

        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Why is this order being returned?");
        Optional<String> input = dialog.showAndWait();

        String item = ordersList.getSelectionModel().getSelectedItem();
        ordersList.getItems().remove(ordersList.getSelectionModel().getSelectedItem());
        for(MenuItem i: itemsList){
            if(i.getName().equals(findName(item))){
                i.setPayed(true);
                itemsList.remove(i);
                return;
            }
        }
    }

    /**
     * This function sets the Items in the selected bar as payed and displays the total that was payed.
     */
    public void pay(){
        currentList.getItems().clear();
        for(MenuItem i: currentItems){
            i.setPayed(true);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Payed bill!" + '\n' + "Total: " + total + ".00 CAD");
        alert.show();
        total=0;

    }
}
