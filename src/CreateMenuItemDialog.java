import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.HashMap;

/**
 * A dialog box that allows the user to create a new Menu Item. This Dialog allows the user to specify the name of the
 * new Menu Item, the Ingredients that make up that Menu Item, and their quantities.
 */
public class CreateMenuItemDialog extends Dialog {

    // Constants.
    final private static String ITEM_NAME_PROMPT_TEXT = "Menu Item name: ";
    final private static String ITEM_COST_PROMPT_TEXT = "Menu Item price: ";
    final private static String INGREDIENT_PROMPT_TEXT = "Ingredients";

    final private static String TABLE_1_NAME = "Inventory";
    final private static String TABLE_2_NAME = "Menu item ingredients";

    final private static String ADD_BUTTON_TEXT = "Add";
    final private static int ADD_BUTTON_WIDTH = 75;

    // The default sizes of the Window.
    final private static int WINDOW_WIDTH = 500;
    final private static int WINDOW_HEIGHT = 400;

    // The minimum width of the table columns.
    final private static int TABLE_COLUMN_MIN_WIDTH = 200;
    // The margins for the content area.
    final private static Insets CONTENT_MARGINS = new Insets(10,10,10,10);

    // Area for user input of the name of the Menu Item.
    private TextField itemNameField;
    private Label itemNamePrompt;

    // Area for user input of the cost of the Menu Item.
    private TextField itemCostField;
    private Label itemCostPrompt;

    // Label for Ingredients area.
    private Label ingredientsPrompt;

    // The table showing all of the Ingredients from the Inventory.
    private TableView<Ingredient> inventoryTable;

    // The table of all the ingredients and their quantities that will be added to the Menu Item.
    private TableView<Ingredient> ingredientsTable;

    // HashMap representing Ingredients to add to Menu Item. Key is the Ingredient, value is the quantity of the
    // Ingredient.
    private HashMap<Ingredient, Integer> ingredientsToAdd;

    // The Manager to use to access and put in information to.
    private Manager manager;

    /**
     * Create a new MenuItemDialog.
     * @param windowName The name of this Window.
     * @param currentManager The Manager that opened this.
     */
    public CreateMenuItemDialog(String windowName, Manager currentManager) {
        super(windowName);
        this.manager = currentManager;
        this.ingredientsToAdd = new HashMap<>();
        super.createAndShow();
        super.setWindowSize(CreateMenuItemDialog.WINDOW_WIDTH, CreateMenuItemDialog.WINDOW_HEIGHT);
    }

    @Override
    public Node createContentPane() {
        // Create the stuff that will be inside of the Dialog box.
        this.itemNameField = new TextField();
        this.itemCostField = new TextField();

        this.itemNamePrompt = new Label(CreateMenuItemDialog.ITEM_NAME_PROMPT_TEXT);
        this.itemNamePrompt.setTextFill(Color.WHITE);
        this.itemCostPrompt = new Label(CreateMenuItemDialog.ITEM_COST_PROMPT_TEXT);
        this.itemCostPrompt.setTextFill(Color.WHITE);
        this.ingredientsPrompt = new Label(CreateMenuItemDialog.INGREDIENT_PROMPT_TEXT);
        this.ingredientsPrompt.setTextFill(Color.WHITE);

        // Disable non-numeric input for item cost.
        this.itemCostField.textProperty().addListener(this.createNumbersOnlyListener(this.itemCostField));

        // Button used to add Ingredients to the Menu Item.
        Button addButton = new Button(CreateMenuItemDialog.ADD_BUTTON_TEXT);
        addButton.setPrefWidth(CreateMenuItemDialog.ADD_BUTTON_WIDTH);
        // When button clicked, add Ingredient to the Menu Item.
        addButton.setOnMouseClicked(event -> this.addButtonClicked());

        // Set up the components inside of a GridPane.
        GridPane contents = new GridPane();
        contents.setAlignment(Pos.CENTER);
        contents.setHgap(10);
        contents.setVgap(10);
        contents.setPadding(CreateMenuItemDialog.CONTENT_MARGINS);

        // Make sure that the content area fills up the entire dialog box.
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(50);
        // There are two columns, so do this twice.
        contents.getColumnConstraints().add(columnConstraints);
        contents.getColumnConstraints().add(columnConstraints);

        // Add stuff to the GridPane.
        contents.add(this.itemNamePrompt, 0, 0);
        contents.add(this.itemNameField, 1, 0);
        contents.add(this.itemCostPrompt, 0, 1);
        contents.add(this.itemCostField, 1, 1);
        contents.add(this.ingredientsPrompt, 0,2);
        // Add the two tables to the GridPane.
        this.initializeInventoryTable();
        contents.add(this.inventoryTable, 0, 3, 1,1);
        this.initializeIngredientsTable();
        contents.add(this.ingredientsTable, 1, 3,1,2);
        // Add button below table.
        contents.add(addButton, 0, 4);

        return contents;
    }

    @Override
    public void processInfo() {
        // Add new Menu Item, if valid.
        String itemName = this.itemNameField.getText();
        if (!this.manager.ourMenu.getMenuItems().containsKey(itemName)) {
            // Catch exception, in case of invalid user input.
            try {
                MenuItem newItem = new MenuItem(itemName, Integer.parseInt(this.itemCostField.getText()),
                        this.ingredientsToAdd);
                // Update the Menu.
                this.manager.ourMenu.getMenuItems().put(newItem.getName(), newItem);
            } catch (NumberFormatException e) {}
        }
    }

    /**
     * Sets up the Ingredients table: the table that shows all of the Ingredients about to be created with the Menu
     * Item.
     */
    private void initializeIngredientsTable() {
        // First generic type is the kind of info being stored, the second is what it is going to display.
        TableColumn<Ingredient, String> nameColumn = new TableColumn<>(CreateMenuItemDialog.TABLE_2_NAME);
        nameColumn.setCellValueFactory(param -> {
            Ingredient ingredient = param.getValue();
            String display = ingredient.getName() + " (" +CreateMenuItemDialog.this.ingredientsToAdd.get(ingredient)
                    + ")";
            return new ReadOnlyStringWrapper(display);
        });
        nameColumn.setMinWidth(CreateMenuItemDialog.TABLE_COLUMN_MIN_WIDTH);

        this.ingredientsTable = new TableView<>();
        this.ingredientsTable.setItems(this.getIngredientList());
        this.ingredientsTable.getColumns().addAll(nameColumn);
    }

    /**
     * Sets up the Inventory table: the table that shows all of the Ingredients in the Inventory.
     */
    private void initializeInventoryTable() {
        // First generic type is the kind of info being stored, the second is what it is going to display.
        TableColumn<Ingredient, String> column = new TableColumn<>(CreateMenuItemDialog.TABLE_1_NAME);
        column.setCellValueFactory(new PropertyValueFactory<>("name"));
        column.setMinWidth(CreateMenuItemDialog.TABLE_COLUMN_MIN_WIDTH);

        this.inventoryTable = new TableView<>();
        this.inventoryTable.setItems(this.getInventoryList());
        this.inventoryTable.getColumns().addAll(column);
    }

    /**
     * Sets up the Ingredients table: the table that shows all of the Ingredients about to be added to the Menu Item.
     */
    private ObservableList<Ingredient> getIngredientList() {
        ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();
        for (Ingredient ingredient : this.ingredientsToAdd.keySet()) {
            ingredients.add(ingredient);
        }
        return ingredients;
    }

    /**
     * Returns all of the Ingredients in the Inventory, in the form of a ObservableList.
     * @return The Ingredients in the Inventory.
     */
    private ObservableList<Ingredient> getInventoryList() {
        ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();
        for (Ingredient ingredient : this.manager.getInventory().getStock()) {
            ingredients.add(ingredient);
        }
        return ingredients;
    }

    /**
     * The add Button was clicked, so add the selected Ingredient to the Menu Item. Updates the Ingredients table
     * accordingly.
     */
    private void addButtonClicked() {
        Ingredient selected = this.inventoryTable.getSelectionModel().getSelectedItem();
        // Make sure something is actually selected.
        if (selected != null) {
            // If selected item already exists, increase it's quantity. Else, just add it.
            if (this.ingredientsToAdd.containsKey(selected)) {
                int oldValue = this.ingredientsToAdd.get(selected);
                this.ingredientsToAdd.put(selected, oldValue + 1);
            } else {
                this.ingredientsToAdd.put(selected, 1);
            }
        }
        // Update.
        this.ingredientsTable.setItems(this.getIngredientList());
        this.ingredientsTable.refresh();
    }

    /**
     * ChangeListener that removes all non-numeric characters from the TextField.
     * @param textField The TextField to remove non-numeric characters from.
     * @return The ChangeListener.
     */
    private ChangeListener<String> createNumbersOnlyListener(TextField textField) {
        ChangeListener<String> numberListener = (observable, oldValue, newValue) -> {
            // Replace all non-numeric characters using regex.
            textField.setText(newValue.replaceAll("[^\\d]", ""));
        };
        return numberListener;
    }

}
