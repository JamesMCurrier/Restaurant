import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class that controls the Manager GUI.
 */
public class ManagerController implements DialogCloseListener {

    // The default widths and heights of the Window upon creation.
    final private static int DEFAULT_WINDOW_WIDTH = 750;
    final private static int DEFAULT_WINDOW_HEIGHT = 500;

    // The text to show at the title screen.
    final private static String TITLE_TEXT = "Manager";
    final private static Font TITLE_FONT = Font.font("Calibri", FontWeight.BOLD, 26);
    final private static Insets TITLE_PADDING = new Insets(25, 50, 25, 50);
    final private static String TITLE_STYLE = "-fx-background-color: #464646;";
    final private static int TITLE_WIDTH = 200;

    // The text for the tab buttons.
    final private static String MENU_TAB_TEXT = "Menu";
    final private static String RESTAURANT_TAB_TEXT = "Restaurant";
    final private static String INVENTORY_TAB_TEXT = "Inventory";
    final private static Insets TAB_BUtTON_PADDING = new Insets(5, 0,0, 0);
    final private static String MENU_TAB_DESCRIPTION = "View the menu. Change and modify Menu Items.";
    final private static String INVENTORY_TAB_DESCRIPTION = "View the Inventory. Change the threshold at which to " +
            "resupply, among other things.";
    final private static String RESTAURANT_TAB_DESCRIPTION = "View Financials and orders currently in the system.";

    // The prompt to tell user to choose who they are.
    final private static String EMPLOYEE_PROMPT_TEXt = "Current Manager:      ";

    // The name of the Dialog Window that pops up when user clicks on buttons before signing in.
    final private static String SIGN_IN_DIALOG_TITLE = "Sign in";

    // The Employee List, which all ManagersGUIs share.
    private static HashMap<String, Manager> employees = new HashMap<>();

    // The Menu Items list, which all Managers share.
    private static HashMap<String, MenuItem> menuItems;

    // All of the Ingredients that are in the Inventory.
    private static ArrayList<Ingredient> ingredients;

    // The tab buttons.
    private Button menuTabButton;
    private Button inventoryTabButton;
    private Button restaurantTabButton;

    // Whether or not the tab buttons are selectable.
    final private static boolean INITIAL_BUTTONS_SELECTABLE = false;
    private boolean buttonsSelectable = ManagerController.INITIAL_BUTTONS_SELECTABLE;

    // The Scene that holds all of the GUI components.
    private Scene mainScene;
    // The layout/pane that holds everything inside of it.
    private BorderPane mainPane;

    // The Manager that is currently using this GUI.
    private Manager currentManager;

    /**
     * Create a new ManagerController. This creates a new Window container the Manger GUI.
     */
    public ManagerController() {
        this.setupScene();
    }

    /**
     * Sets up the Manager GUI.
     */
    private void setupScene() {
        this.mainPane = new BorderPane();

        // Title.
        Label title = new Label(TITLE_TEXT);
        title.setTextFill(Color.WHITE);
        title.setFont(TITLE_FONT);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setPrefWidth(ManagerController.TITLE_WIDTH);
        HBox topContainer = new HBox();
        topContainer.getChildren().add(title);
        topContainer.setAlignment(Pos.CENTER_LEFT);
        topContainer.setStyle(TITLE_STYLE);
        this.mainPane.setTop(topContainer);

        // Allow user to choose which Manager they are.
        Label employeePrompt = new Label(ManagerController.EMPLOYEE_PROMPT_TEXt);
        employeePrompt.setTextFill(Color.WHITE);
        topContainer.getChildren().add(employeePrompt);
        ComboBox<String> employeeComboBox = new ComboBox<>();
        employeeComboBox.setPrefWidth(ManagerController.TITLE_WIDTH);
        employeeComboBox.getItems().addAll(this.getEmployees());
        topContainer.getChildren().add(employeeComboBox);

        // Tabs on the left.
        VBox leftContainer = new VBox();

        // Create the buttons.
        this.menuTabButton = ManagerGUIFactory.createTabButton(MENU_TAB_TEXT, MENU_TAB_DESCRIPTION);
        this.inventoryTabButton = ManagerGUIFactory.createTabButton(INVENTORY_TAB_TEXT, INVENTORY_TAB_DESCRIPTION);
        this.restaurantTabButton = ManagerGUIFactory.createTabButton(RESTAURANT_TAB_TEXT, RESTAURANT_TAB_DESCRIPTION);

        // When user clicks on one of the Tab Buttons, change the content pane accordingly, as long as the buttons
        // are selectable.
        this.menuTabButton.setOnMouseClicked(e -> {
            if (this.buttonsSelectable) {
                this.mainPane.setCenter(this.createMenuPane().getContentPane());
            } else {
                Dialog dialog = new ManagerSignInDialog(ManagerController.SIGN_IN_DIALOG_TITLE);
            }
        });
        this.inventoryTabButton.setOnMouseClicked(e -> {
            if (this.buttonsSelectable) {
                this.mainPane.setCenter(this.createInventoryPane().getContentPane());
            } else {
                Dialog dialog = new ManagerSignInDialog(ManagerController.SIGN_IN_DIALOG_TITLE);
            }
        });
        this.restaurantTabButton.setOnMouseClicked(e -> {
            if (this.buttonsSelectable) {
                this.mainPane.setCenter(this.createRestaurantPane().getContentPane());
            } else {
                Dialog dialog = new ManagerSignInDialog(ManagerController.SIGN_IN_DIALOG_TITLE);
            }
        });

        // As soon as the User signs in, allow them to click on the tab buttons, and update the current user.
        employeeComboBox.valueProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem != null) {
                this.buttonsSelectable = true;
                // Set the new user.
                this.currentManager = ManagerController.employees.get(newItem);
            }
        });

        // Add tab buttons.
        leftContainer.getChildren().add(menuTabButton);
        leftContainer.getChildren().add(inventoryTabButton);
        leftContainer.getChildren().add(restaurantTabButton);
        leftContainer.setStyle(TITLE_STYLE);
        // Set padding.
        leftContainer.setPadding(TAB_BUtTON_PADDING);
        this.mainPane.setLeft(leftContainer);

        // Further modify components.
        HBox.setMargin(title, TITLE_PADDING);
        HBox.setHgrow(topContainer, Priority.ALWAYS);

        // Add default content pane.
        BorderPane contentPane = ManagerGUIFactory.createDefaultContentPane();
        this.mainPane.setCenter(contentPane);

        // Create the Scene.
        this.mainScene = new Scene(this.mainPane, ManagerController.DEFAULT_WINDOW_WIDTH,
                ManagerController.DEFAULT_WINDOW_HEIGHT);
    }

    /**
     * Creates and returns a Inventory Pane.
     * @return The Inventory content pane.
     */
    private ManagerGUIContentPane createInventoryPane() {
        ManagerGUIContentPane inventoryPane = ManagerGUIFactory.createInventoryContentPane(
                this.currentManager.getInventory().getStock());
        // Add buttons.
        Button newButton = inventoryPane.createMenuButton("New");
        newButton.setOnMouseClicked(e -> {
            // Open Dialog.
            Dialog dialog = new CreateIngredientDialog("Create Ingredient",
                    this.currentManager.getInventory());
            dialog.setListener(this);
        });
        Button editButton = inventoryPane.createMenuButton("Edit");
        editButton.setOnMouseClicked(e -> {
            // Open Dialog if something was selected.
            if (inventoryPane.getSelectedItem() != null) {
                String itemName = inventoryPane.getSelectedItem().toLowerCase();
                Ingredient ingredient = this.currentManager.getInventory().getIngredient(itemName);
                Dialog dialog = new EditInventoryDialog("Edit Ingredient",
                        ingredient);
                dialog.setListener(this);
            }
        });
        Button viewButton = inventoryPane.createMenuButton("Requests");
        viewButton.setOnMouseClicked(e -> {
            Dialog dialog = new RequestsDialog("Request Items", this.currentManager.getInventory());
            dialog.setListener(this);
        });
        return inventoryPane;
    }

    /**
     * Creates and returns a Menu Pane.
     * @return The Menu content pane.
     */
    private ManagerGUIContentPane createMenuPane() {
        ManagerGUIContentPane menuPane = ManagerGUIFactory.createMenuContentPane(this.currentManager.ourMenu);
        Button newButton = menuPane.createMenuButton("New");
        newButton.setOnMouseClicked(e -> {
            Dialog dialog = new CreateMenuItemDialog("Create new Menu Item", this.currentManager);
            dialog.setListener(this);
        });
        return menuPane;
    }

    /**
     * Creates and returns a Restaurant Pane.
     * @return The Restaurant content pane.
     */
    private ManagerGUIContentPane createRestaurantPane() {
        ManagerGUIContentPane restaurantPane = ManagerGUIFactory.createRestaurantContentPane();
        // Update the Restaurant Pane with all the Orders currently in the system.
        ArrayList<Order> orders = this.currentManager.getActiveOrders();
        HashMap<String, String> orderDisplay = new HashMap<>();
        for (Order order : orders) {
            String description = order.toString();
            String orderName = "Order " + order.getId();
            orderDisplay.put(orderName, description);
        }
        restaurantPane.setItemsToDisplay(orderDisplay);
        return restaurantPane;
    }

    /**
     * Given a list of employees, adds all Manager employees to the static Map of employees.
     * @param employees List of employees of which to add.
     */
    public static void setEmployeesList(ArrayList<Employee> employees){
        for(Employee employee : employees){
            if (employee instanceof Manager) {
                ManagerController.employees.put(employee.getName(), (Manager) employee);
            }
        }
    }

    /**
     * Given a Map of MenuItems, set them to the static Map of menuItems.
     * @param menuItems Map of Menu Items to be set.
     */
    public static void setMenuItemsList(HashMap<String, MenuItem> menuItems){
        ManagerController.menuItems = menuItems;
    }

    /**
     * Given an ArrayList of Ingredients, set them to the static ArrayList of Ingredients.
     * @param ingredients All of the Ingredients from the Inventory.
     */
    public static void setIngredientList(ArrayList<Ingredient> ingredients) {
        ManagerController.ingredients = ingredients;
    }

    /**
     * Returns the list of Manager names, in the form of an ObservableList.
     * @return The managers.
     */
    private ObservableList<String> getEmployees() {
        ObservableList<String> employees = FXCollections.observableArrayList();
        for (String employee : ManagerController.employees.keySet()) {
            employees.add(employee);
        }
        return employees;
    }

    /**
     * Returns the Scene that represents this ManagerController.
     * @return The Scene.
     */
    public Scene getScene() {
        return this.mainScene;
    }

    @Override
    public void dialogClosed(Dialog dialog) {
        // Based on the Dialog that was just closed, update the content area.
        if (dialog instanceof CreateIngredientDialog) {
            this.mainPane.setCenter(createInventoryPane().getContentPane());
        }
        if (dialog instanceof EditInventoryDialog) {
            this.mainPane.setCenter(createInventoryPane().getContentPane());
        }
        if (dialog instanceof CreateMenuItemDialog) {
            this.mainPane.setCenter(createMenuPane().getContentPane());
        }
    }
}