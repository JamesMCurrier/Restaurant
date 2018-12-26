import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.HashMap;

public class ManagerGUIContentPane {

    // The colours for the menu.
    final private static String BOTTOM_MENU_STYLE = "-fx-background-color: #464646;";
    final private static String CONTENT_STYLE = "-fx-background-color: #464646; -fx-border-color: #fafffe;" +
            "-fx-border-insets: 5;" + "-fx-border-width: 2;" + "-fx-border-style: dashed;" + "-fx-padding: 15;";
    // The style for the menu buttons.
    final private static Insets BOTTOM_CONTAINER_MARGINS = new Insets(20,20,5,20);
    // The size for the menu buttons.
    final private static int MENU_BUTTON_WIDTH = 100;

    // The main pane.
    private BorderPane contentPane;

    // The bottom container, which holds all of the buttons.
    private HBox bottomContainer;

    // The list to display.
    private ListView displayList;
    private ObservableList<String> itemNames;
    // Store the items to display. Key represents the name of the item, and value is the description of the item.
    private HashMap<String, String> itemsToDisplay;
    // Text to display when the list is empty.
    final private static String LIST_EMPTY_TEXT = "There is currently nothing to display.";

    // The title of this pane.
    private String title;
    // The font to display it with.
    private Font TITLE_FONT = Font.font("Calibri", FontWeight.BOLD, 26);

    public ManagerGUIContentPane(String title, HashMap<String, String> displayItems) {
        this.title = title;
        this.contentPane = new BorderPane();
        this.displayList = new ListView();
        this.itemsToDisplay = displayItems;
        this.itemNames = FXCollections.observableArrayList(this.itemsToDisplay.keySet());
        setupContentPane();
    }

    private void setupContentPane() {

        // If there is nothing to display, have first cell print out message.
        if (itemNames.size() == 0) {
            itemNames.add("There is currently nothing to display.");
        }
        // Setup the list.
        this.displayList.setItems(itemNames);
        // Modify look of the cells.
        this.setupCells();

        // Add list to content pane.
        this.contentPane.setCenter(this.displayList);

        // The menu on the right holding all the buttons.
        this.bottomContainer = new HBox();
        this.bottomContainer.setPadding(ManagerGUIContentPane.BOTTOM_CONTAINER_MARGINS);
        this.bottomContainer.setSpacing(10);
        this.bottomContainer.setStyle(BOTTOM_MENU_STYLE);
        this.bottomContainer.setAlignment(Pos.CENTER);
        this.contentPane.setBottom(this.bottomContainer);

        // The title.
        Text title = new Text(this.title);
        title.setFont(TITLE_FONT);
        title.setFill(Color.WHITE);
        this.contentPane.setTop(title);

        this.contentPane.setStyle(CONTENT_STYLE);
    }

    /**
     * Sets up each cell in the list.
     */
    private void setupCells() {
        // Modify look of the cells.
        this.displayList.setCellFactory(param -> new ListCell<String>() {
            // Pane to hold everything.
            BorderPane cellContents = new BorderPane();
            // The name of the menu item.
            Text itemName = new Text();
            // Additional details.
            Label description = new Label();

            @Override
            public void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                // Cell is empty; don't display anything.
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Update information to display.
                    itemName.setText(name);

                    // The description of the item.
                    String descriptionText = ManagerGUIContentPane.this.itemsToDisplay.get(name);
                    description.setText(descriptionText);
                    description.setWrapText(true);

                    // Display the information.
                    cellContents.setTop(itemName);
                    if (descriptionText != null) {
                        cellContents.setCenter(description);
                        cellContents.setAlignment(description, Pos.CENTER_LEFT);
                    }
                    setGraphic(cellContents);
                }
            }
        });
    }

    /**
     * Creates a menu button, display it in the bottom container of the Content Pane, then returns it.
     * @param buttonText The text to display.
     * @return The Button.
     */
    public Button createMenuButton(String buttonText) {
        Button button = new Button(buttonText);
        button.setPrefWidth(ManagerGUIContentPane.MENU_BUTTON_WIDTH);
        this.bottomContainer.getChildren().add(button);
        return button;
    }

    /**
     * Sets the things to display, in the form of a HashMap. Redraws the list accordingly.
     * @param items A HashMap containing the things to display. The key is the title, while the value is the
     * description.
     */
    public void setItemsToDisplay(HashMap<String, String> items) {
        this.itemsToDisplay = items;
        this.itemNames = FXCollections.observableArrayList(this.itemsToDisplay.keySet());
        // If there is nothing to display, have first cell print out message.
        if (itemNames.size() == 0) {
            itemNames.add(ManagerGUIContentPane.LIST_EMPTY_TEXT);
        }
        this.displayList.setItems(this.itemNames);
        this.setupCells();
    }

    /**
     * Return the String name of the currently selected item.
     * @return The currently selected item.
     */
    public String getSelectedItem() {
        return (String) this.displayList.getSelectionModel().getSelectedItem();
    }

    public BorderPane getContentPane() {
        return this.contentPane;
    }

}
