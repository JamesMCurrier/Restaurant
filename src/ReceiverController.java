//import com.sun.webkit.dom.RectImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ReceiverController {
    @FXML
    private ComboBox<String> employeeComboBox;
    @FXML
    private ListView<String> itemsList;
    private static HashMap<String, Receiver> employees = new HashMap<>();
    private static Employee currEmployee;
    @FXML
    private Button refreshButton;

    /**
     * Given a list of employees, adds all type Receiver employees to the static Map of employees.
     * @param employees List of employees of which to add.
     */
    public static void setEmployeesList(ArrayList<Employee> employees){
        for(Employee employee : employees){
            if(employee instanceof Receiver) {
                ReceiverController.employees.put(employee.getName(), (Receiver)employee);
            }
        }
    }

    /**
     * Adds the names of all servers On the GUI and sets all the menu items.
     */
    public void initializeReceiverScene(){
        if(employeeComboBox.getItems().size() == 0) {
            for (String receiverName : employees.keySet()) {
                employeeComboBox.getItems().add(receiverName);
            }
        }
    }

    /**
     * Refreshes the list of ingredients to be ordered
     */
    public void refresh(){
        itemsList.getItems().clear();
        itemsList.getItems().add(currEmployee.getInventory().getLowStock());

    }

    /**
     * Restock the inventory.
     */
    public void restockItem(){

    }

    public void setCurrEmployee(){
        String rName = ((ComboBox) employeeComboBox).getSelectionModel().getSelectedItem().toString();
        currEmployee = (Employee) employees.get(rName);
        refreshButton.disableProperty().setValue(false);
    }
}
