import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ChefController {
    @FXML
    private ComboBox<String> employeeComboBox;
    @FXML
    private ListView<String> ordersList;
    @FXML
    private Button viewNewOrdersButton, viewDetailsButton, orderReadyButton, claimOrderButton;
    @FXML
    private Label isEmpty;

    private Chef currChef;

    private static HashMap<String, Chef> employees = new HashMap<>();

    public static void setEmployeesList(ArrayList<Employee> employees) {
        for (Employee employee : employees) {
            if (employee instanceof Chef) {
                ChefController.employees.put(employee.getName(), (Chef) employee);
            }
        }
    }

    public void initBox() {
        if (employeeComboBox.getItems().size() == 0) {
            for (String chefName : employees.keySet()) {
                employeeComboBox.getItems().add(chefName);
            }

        }
    }

        public void viewNewOrders() {
            if (currChef.getOrdersWaiting().size() != 0){
                ordersList.getItems().clear();

                for (Order order : currChef.getOrdersWaiting()){
                    ordersList.getItems().add(order.toString());
                }
                isEmpty.visibleProperty().setValue(false);
            }
            else{
                ordersList.getItems().clear();
                isEmpty.visibleProperty().setValue(true);
            }
        }

        public void viewDetails() {
            String str = ordersList.getSelectionModel().getSelectedItem();
            String [] lis = str.split(" ");
            lis = lis[2].split(",");
            int num = Integer.parseInt(lis[0]);
            String titleStr = "Order #" + num;
            Order order = currChef.getOrderWaiting(num);

            String info = "";

            for (MenuItem mu : order.getUnpaidItems()){
                info += mu.getName();
                info += " => ";
                HashMap ings = new HashMap<String, Integer>();


                for (Ingredient ing : mu.getIngredients().keySet()){
                    ings.put(ing.getName(), mu.getIngredients().get(ing));
                }

                info += ings;
                info += "\n";
            }


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(titleStr);
            alert.setContentText(info);
            alert.show();
        }

        public void claimOrder() {
            String str = ordersList.getSelectionModel().getSelectedItem();
            String [] lis = str.split(" ");
            lis = lis[2].split(",");
            int num = Integer.parseInt(lis[0]);
            Event e = new Event(EventType.REMOVE_ORDER);
            e.addOrder(currChef.getOrderWaiting(num));
            e.setExp(currChef.getName());
            currChef.sendEvent(e);
            viewNewOrders();
        }

        public void orderReady() {
        try {
            String str = ordersList.getSelectionModel().getSelectedItem();
            String[] lis = str.split(" ");
            lis = lis[2].split(",");
            int num = Integer.parseInt(lis[0]);
            currChef.orderReady(currChef.getOrderWaiting(num));
            viewNewOrders();
            itemSelect();
        }
        catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please Select Order.");
            alert.show();
        }}

        public void setCurrChef() {
            String chefName = ((ComboBox) employeeComboBox).getSelectionModel().getSelectedItem().toString();
            currChef = employees.get(chefName);
            viewNewOrdersButton.disableProperty().setValue(false);
            viewNewOrders();
            itemSelect();
        }


        public void itemSelect(){
            if (ordersList.getSelectionModel().getSelectedItem() != null){
                orderReadyButton.disableProperty().setValue(false);
                viewDetailsButton.disableProperty().setValue(false);
                claimOrderButton.disableProperty().setValue(false);
            }
            else{
                orderReadyButton.disableProperty().setValue(true);
                viewDetailsButton.disableProperty().setValue(true);
                claimOrderButton.disableProperty().setValue(true);
            }
        }
    }
