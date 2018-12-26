import javafx.beans.value.ChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

/**
 * A Dialog that allows users to create new Ingredients and add them to the Inventory.
 */
public class CreateIngredientDialog extends Dialog {
    // Constants.
    final private static String NAME_LABEL = "Ingredient name: ";
    final private static String CURRENT_AMOUNT_LABEL = "Current amount: ";
    final private static String RESTOCK_THRESHOLD_LABEL = "Restock Threshold: ";
    final private static String REQUEST_AMOUNT_LABEL = "Request Amount: ";

    // The margins for the content area.
    final private static Insets CONTENT_MARGINS = new Insets(10,10,10,10);

    // The default sizes of the Window.
    final private static int WINDOW_WIDTH = 450;
    final private static int WINDOW_HEIGHT = 300;


    // The name of the Ingredient.
    private Label nameLabel;
    private TextField nameField;

    // The amount of the Ingredient in the Inventory.
    private Label currentAmountLabel;
    private TextField currentAmountField;

    // The threshold to reach before restocking.
    private Label restockThresholdLabel;
    private TextField restockThresholdField;

    // The amount to request when a request is sent.
    private Label requestAmountLabel;
    private TextField requestAmountField;

    // The Inventory to add the Ingredient to.
    private Inventory inventory;

    /**
     * Create a new CreateIngredientDialog, which allows users to create new Ingredients.
     * @param windowName The name of the Window.
     * @param inventory The Inventory to create the Ingredient for.
     */
    public CreateIngredientDialog(String windowName, Inventory inventory) {
        super(windowName);
        this.inventory = inventory;
        super.createAndShow();
        super.setWindowSize(CreateIngredientDialog.WINDOW_WIDTH, CreateIngredientDialog.WINDOW_HEIGHT);
    }

    @Override
    public Node createContentPane() {
        // Set up the labels.
        this.nameLabel = new Label(CreateIngredientDialog.NAME_LABEL);
        this.currentAmountLabel = new Label(CreateIngredientDialog.CURRENT_AMOUNT_LABEL);
        this.restockThresholdLabel = new Label(CreateIngredientDialog.RESTOCK_THRESHOLD_LABEL);
        this.requestAmountLabel = new Label(CreateIngredientDialog.REQUEST_AMOUNT_LABEL);
        this.nameLabel.setTextFill(Color.WHITE);
        this.currentAmountLabel.setTextFill(Color.WHITE);
        this.restockThresholdLabel.setTextFill(Color.WHITE);
        this.requestAmountLabel.setTextFill(Color.WHITE);

        // Sets up the TextFields for user input.
        this.nameField = new TextField();
        this.currentAmountField = new TextField();
        this.restockThresholdField = new TextField();
        this.requestAmountField = new TextField();

        // Prevent user from entering in non-numeric characters.
        this.currentAmountField.textProperty().addListener(this.createNumbersOnlyListener(this.currentAmountField));
        this.restockThresholdField.textProperty().addListener(this.createNumbersOnlyListener(
                this.restockThresholdField));
        this.requestAmountField.textProperty().addListener(this.createNumbersOnlyListener(this.requestAmountField));

        // The GridPane to hold everything the components for user input.
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(CreateIngredientDialog.CONTENT_MARGINS);

        // Make sure that the content area fills up the entire dialog box.
        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setPercentWidth(50);
        gridPane.getColumnConstraints().add(columnConstraints1);
        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setPercentWidth(50);
        gridPane.getColumnConstraints().add(columnConstraints2);

        // Add stuff to the GridPane.
        gridPane.add(this.nameLabel, 0,0);
        gridPane.add(this.nameField, 1, 0);
        gridPane.add(this.currentAmountLabel, 0, 1);
        gridPane.add(this.currentAmountField, 1, 1);
        gridPane.add(this.restockThresholdLabel, 0, 2);
        gridPane.add(this.restockThresholdField, 1, 2);
        gridPane.add(this.requestAmountLabel, 0, 3);
        gridPane.add(this.requestAmountField, 1, 3);

        gridPane.setHalignment(this.nameLabel, HPos.CENTER);
        gridPane.setHalignment(this.currentAmountLabel, HPos.CENTER);
        gridPane.setHalignment(this.restockThresholdLabel, HPos.CENTER);
        gridPane.setHalignment(this.requestAmountLabel, HPos.CENTER);

        // Fill parent.
        gridPane.setHgrow(this.currentAmountField, Priority.ALWAYS);

        // The content pane that holds everything.
        BorderPane contents = new BorderPane();
        contents.setCenter(gridPane);
        contents.setAlignment(this.nameLabel, Pos.BOTTOM_CENTER);
        contents.setAlignment(gridPane, Pos.BOTTOM_CENTER);

        return contents;
    }

    @Override
    public void processInfo() {
        // Make sure info is valid.
        if (this.getName().equals("") | this.getCurrentAmount() < 0 | this.getRestockAmount() < 0 |
                this.getRestockAmount() < 0) {
            return;
        }

        // Create an Ingredient based on the information.
        Ingredient ingredient = new Ingredient(this.getName(), this.getCurrentAmount(), this.getRestockThreshold(),
                this.getRestockAmount());
        // Only add this Ingredient to the Inventory if it does not already exist.
        if (!this.inventory.getIngredients().containsKey(ingredient.getName())) {
            this.inventory.getIngredients().put(ingredient.getName(), ingredient);
        }
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

    /**
     * Return the name of the Ingredient.
     * @return The name.
     */
    private String getName() {
        return this.nameField.getText();
    }

    /**
     * Return the current amount.
     * @return The current amount.
     */
    private int getCurrentAmount() {
        if (this.currentAmountField.getText().equals("")) {
            return -1;
        }
        return Integer.parseInt(this.currentAmountField.getText());
    }

    /**
     * Return the restock threshold.
     * @return The restock threshold.
     */
    private int getRestockThreshold() {
        if (this.restockThresholdField.getText().equals("")) {
            return -1;
        }
        return Integer.parseInt(this.restockThresholdField.getText());
    }

    /**
     * Return the restock amount.
     * @return The restock amount.
     */
    private int getRestockAmount() {
        if (this.requestAmountField.getText().equals("")) {
            return -1;
        }
        return Integer.parseInt(this.requestAmountField.getText());
    }
}
