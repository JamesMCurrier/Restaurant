import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class that provides factory methods to create parts of the Manager GUI.
 */
public class ManagerGUIFactory {

    final private static int TAB_BUTTON_WIDTH = 200;

    // --- Constants for Tab Buttons ---

    // Tab Button Styles.
    // Note about padding: to prevent button from expanding over limit, make sure that the sum of left and right padding
    // are equal across all styles. Otherwise, the button will expand out of bounds, and will appear to be wider than
    // all of the other buttons, which does not look good.
    final private static String TAB_BUTTON_UNCLICKED_STYLE = "-fx-background-color:#90EE90; " +
            "-fx-background-radius: 0px, 0px, 0px, 0px;"
            + "-fx-padding: 20 20 20 20";
    final private static String TAB_BUTTON_HOVER_STYLE = "-fx-background-color: #89ff89;" +
            "-fx-background-insets: 0 0 0 5, 0, 0, 0;" + "-fx-background-radius: 0px, 0px, 0px, 0px;"
            + "-fx-padding: 30 10 30 30";
    final private static String TAB_BUTTON_CLICK_STYLE = "-fx-background-color: #5ab45a;" +
            "-fx-background-insets: 0 0 0 5, 0, 0, 0;" + "-fx-background-radius: 0px, 0px, 0px, 0px;" +
            "-fx-padding: 30 10 30 30";
    final private static Font TAB_BUTTON_NAME_FONT = Font.font("Calibri", FontWeight.SEMI_BOLD, 24);

    // --- Constants for Default Content Pane ---

    // Text to display in the default content pane.
    final private static String DEFAULT_CONTENT_TEXT = "Welcome, Manager! To get started, click on one of the options" +
            " on the left.";
    final private static Font DEFAULT_CONTENT_FONT = Font.font("Calibri", FontWeight.BOLD, 26);
    // The colours for the default content pane.
    final private static String DEFAULT_CONTENT_STYLE = "-fx-background-color: #464646; -fx-border-color: #fafffe;" +
            "-fx-border-insets: 5;" + "-fx-border-width: 3;" + "-fx-border-style: dashed;" + "-fx-padding: 15;";
    // The style for the text in the default content pane.
    final private static String DEFAULT_CONTENT_TEXT_STYLE = "-fx-padding: 20 30 60 30";

    final private static String MENU_PANE_TITLE = "Menu";
    final private static String INVENTORY_PANE_TITLE = "Inventory";
    final private static String RESTAURTANT_PANE_TITLE = "Orders currently in system";


    // Temporary Strings to test.
    static String[] inventoryItems = {"Buns", "Patties", "Beans"};
    static String[] description2 = {"Current Amount: 10","Current Amount: 26","Current Amount: 15"};

    /**
     * Creates and returns a Tab Button.
     * @param name The name of the tab.
     * @param description The description of the tab.
     * @return The tab Button.
     */
    public static Button createTabButton(String name, String description) {
        // The text description of the button.
        Text textDescription = new Text(description);
        textDescription.setWrappingWidth(TAB_BUTTON_WIDTH);

        // The title of the button.
        Text buttonTitle = new Text(name);
        buttonTitle.setFont(TAB_BUTTON_NAME_FONT);

        // Pane that stores contents of the button.
        BorderPane buttonContents = new BorderPane();
        buttonContents.setTop(buttonTitle);
        buttonContents.setCenter(textDescription);

        // Create the button with a title and a description.
        Button button = new Button("", buttonContents);
        button.setAlignment(Pos.BASELINE_LEFT);
        button.setPrefWidth(TAB_BUTTON_WIDTH);

        // Sets the colour for the various states of the button.
        button.setStyle(TAB_BUTTON_UNCLICKED_STYLE);
        button.setOnMouseEntered(e -> button.setStyle(TAB_BUTTON_HOVER_STYLE));
        button.setOnMouseExited(e -> button.setStyle(TAB_BUTTON_UNCLICKED_STYLE));
        button.setOnMousePressed(e -> button.setStyle(TAB_BUTTON_CLICK_STYLE));
        button.setOnMouseReleased(e -> button.setStyle(TAB_BUTTON_HOVER_STYLE));
        return button;
    }

    /**
     * Creates and returns a default content pane, which directs the user as to what to do.
     * @return The content pane.
     */
    public static BorderPane createDefaultContentPane() {
        BorderPane contentPane = new BorderPane();
        Label textContent = new Label(DEFAULT_CONTENT_TEXT);
        textContent.setWrapText(true);
        textContent.setFont(DEFAULT_CONTENT_FONT);
        textContent.setTextFill(Color.WHITE);
        textContent.setStyle(DEFAULT_CONTENT_TEXT_STYLE);
        contentPane.setCenter(textContent);
        contentPane.setStyle(DEFAULT_CONTENT_STYLE);
        return contentPane;
    }

    /**
     * Creates and returns a new Menu Content Pane. A Menu Content Pane consists of a list of all the Menu Items in
     * the menu.
     * @param menu A HashMap that stores the name of each MenuItem as a key, and the MenuItem object as the instance.
     * @return The ManagerGUIContentPane.
     */
    public static ManagerGUIContentPane createMenuContentPane(Menu menu) {

        // Get the Menu Items.
        HashMap<String, MenuItem> menuItems = menu.getMenuItems();

        // HashMap that represents the contents of each list item. The key represent name of the item, while the
        // corresponding value is the description.
        HashMap<String, String> contents = new HashMap<>(0);

        // Loop through each MenuItem to get required data.
        for (String itemName : menuItems.keySet()) {
            String description = "";
            MenuItem item = menuItems.get(itemName);
            description += "Cost: " + item.getCost() + " | Ingredients:";

            // Loop through each Ingredient in this MenuItem to get data.
            for (Ingredient ingredient : item.getIngredients().keySet()) {
                int amount = item.getIngredients().get(ingredient);
                description += " | " + ingredient.getName() + " (" + amount + ")";
            }
            contents.put(itemName, description);
        }

        ManagerGUIContentPane menuPane = new ManagerGUIContentPane(MENU_PANE_TITLE, contents);
        return menuPane;
    }

    /**
     * Creates and returns a new Inventory Content Pane. A Inventory Content Pane consists of a list of all Ingredients
     * in the Inventory.
     * @param ingredients A ArrayList that stores all of the Ingredients from the Inventory.
     * @return The ManagerGUIContentPane.
     */
    public static ManagerGUIContentPane createInventoryContentPane(ArrayList<Ingredient> ingredients) {
        // HashMap that represents the contents of each list item. The key represent name of the Ingredient, while the
        // corresponding value is the description, which includes the current amount in the Inventory, etc.
        HashMap<String, String> contents = new HashMap<>(0);

        for (Ingredient ingredient : ingredients) {
            String description = "";
            description += "Current amount: " + ingredient.getAmount();
            description += " | Threshold at which needs to be restocked: " + ingredient.getRestockThreshold();
            description += " | Amount to order when requesting: " + ingredient.getRequestAmount();
            String name = ingredient.getName();
            // Capitalize first letter of Ingredient name.
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            contents.put(name, description);
        }

        ManagerGUIContentPane inventoryPane = new ManagerGUIContentPane(INVENTORY_PANE_TITLE, contents);
        return inventoryPane;
    }

    /**
     * Creates and returns a new Restaurant Content Pane. A Restaurant Content Pane consists of a list of all
     * active Orders in the Restaurant.
     * @return The ManagerGUIContentPane.
     */
    public static ManagerGUIContentPane createRestaurantContentPane() {
        HashMap<String, String> contents = new HashMap<>();
        ManagerGUIContentPane restaurantPane = new ManagerGUIContentPane(RESTAURTANT_PANE_TITLE, new HashMap<>());
        return restaurantPane;
    }
}
