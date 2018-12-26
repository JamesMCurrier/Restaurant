import javafx.beans.value.ChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * A Dialog that allows users to edit a specific item from the Inventory.
 */
public class EditInventoryDialog extends Dialog {

    // Constants.
    final private static String CURRENT_AMOUNT_LABEL = "Current amount: ";
    final private static String RESTOCK_THRESHOLD_LABEL = "Restock Threshold: ";
    final private static String REQUEST_AMOUNT_LABEL = "Request Amount: ";

    // The margins for the content area.
    final private static Insets CONTENT_MARGINS = new Insets(10,10,10,10);

    // The default sizes of the Window.
    final private static int WINDOW_WIDTH = 450;
    final private static int WINDOW_HEIGHT = 300;

    // The height of the name label that displays name of Ingredient.
    final private static int NAME_LABEL_HEIGHT = 20;
    final private static Font NAME_LABEL_FONT = Font.font(20);
    final private static Insets NAME_LABEL_MARGINS = new Insets(50,0,0,0);

    // The name of the Ingredient.
    private Label nameLabel;

    // The amount of the Ingredient in the Inventory.
    private Label currentAmountLabel;
    private TextField currentAmountField;

    // The threshold to reach before restocking.
    private Label restockThresholdLabel;
    private TextField restockThresholdField;

    // The amount to request when a request is sent.
    private Label requestAmountLabel;
    private TextField requestAmountField;

    // The Ingredient that this Dialog is supposed to modify.
    private Ingredient ingredient;

    /**
     * Create a new EditInventoryDialog, which allows users to edit a specific Ingredient.
     * @param windowName The name of the Window.
     * @param ingredient The Ingredient to edit.
     */
    public EditInventoryDialog(String windowName, Ingredient ingredient) {
        super(windowName);
        this.ingredient = ingredient;
        super.createAndShow();
        super.setWindowSize(EditInventoryDialog.WINDOW_WIDTH, EditInventoryDialog.WINDOW_HEIGHT);
    }

    @Override
    public Node createContentPane() {
        // Set up the labels.
        this.nameLabel = new Label(this.ingredient.getName());
        this.currentAmountLabel = new Label(EditInventoryDialog.CURRENT_AMOUNT_LABEL);
        this.restockThresholdLabel = new Label(EditInventoryDialog.RESTOCK_THRESHOLD_LABEL);
        this.requestAmountLabel = new Label(EditInventoryDialog.REQUEST_AMOUNT_LABEL);
        this.nameLabel.setTextFill(Color.WHITE);
        this.currentAmountLabel.setTextFill(Color.WHITE);
        this.restockThresholdLabel.setTextFill(Color.WHITE);
        this.requestAmountLabel.setTextFill(Color.WHITE);

        // Sets up the TextFields for user input.
        this.currentAmountField = new TextField(String.valueOf(this.ingredient.getAmount()));
        this.restockThresholdField = new TextField(String.valueOf(this.ingredient.getRestockThreshold()));
        this.requestAmountField = new TextField(String.valueOf(this.ingredient.getRequestAmount()));

        // Disable user from editing Ingredient amount.
        this.currentAmountField.setEditable(false);
        this.currentAmountField.setDisable(true);

        // Prevent user from entering in non-numeric characters.
        this.restockThresholdField.textProperty().addListener(this.createNumbersOnlyListener(
                this.restockThresholdField));
        this.requestAmountField.textProperty().addListener(this.createNumbersOnlyListener(this.requestAmountField));

        // Properties of the title.
        this.nameLabel.setMinHeight(EditInventoryDialog.NAME_LABEL_HEIGHT);
        this.nameLabel.setFont(EditInventoryDialog.NAME_LABEL_FONT);
        this.nameLabel.setPadding(EditInventoryDialog.CONTENT_MARGINS);

        // The GridPane to hold everything the components for user input.
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(EditInventoryDialog.CONTENT_MARGINS);

        // Make sure that the content area fills up the entire dialog box.
        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setPercentWidth(50);
        gridPane.getColumnConstraints().add(columnConstraints1);
        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setPercentWidth(50);
        gridPane.getColumnConstraints().add(columnConstraints2);

        // Add stuff to the GridPane.
        gridPane.add(this.currentAmountLabel, 0, 1);
        gridPane.add(this.currentAmountField, 1, 1);
        gridPane.add(this.restockThresholdLabel, 0, 2);
        gridPane.add(this.restockThresholdField, 1, 2);
        gridPane.add(this.requestAmountLabel, 0, 3);
        gridPane.add(this.requestAmountField, 1, 3);

        gridPane.setHalignment(this.currentAmountLabel, HPos.CENTER);
        gridPane.setHalignment(this.restockThresholdLabel, HPos.CENTER);
        gridPane.setHalignment(this.requestAmountLabel, HPos.CENTER);

        // Fill parent.
        gridPane.setHgrow(this.currentAmountField, Priority.ALWAYS);

        // The content pane that holds everything.
        BorderPane contents = new BorderPane();
        contents.setTop(this.nameLabel);
        contents.setCenter(gridPane);
        contents.setAlignment(this.nameLabel, Pos.BOTTOM_CENTER);
        contents.setAlignment(gridPane, Pos.BOTTOM_CENTER);

        return contents;
    }

    @Override
    public void processInfo() {

        // Make sure info is valid.
        if (this.getRestockAmount() < 0 | this.getRestockAmount() < 0) {
            return;
        }

        // Set the new Ingredient information.
        int restockThreshold = this.getRestockThreshold();
        this.ingredient.setRestockThreshold(restockThreshold);
        int restockAmount = this.getRestockAmount();
        this.ingredient.setRequestAmount(restockAmount);
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