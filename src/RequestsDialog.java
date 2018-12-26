import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * A Dialog that displays Ingredients that need to be ordered. It allows users to copy and paste the required
 * information into their E-Mails.
 */
public class RequestsDialog extends Dialog {

    // Constants.
    final private static String TITLE_TEXT = "These are the Ingredients that need to be requested. Copy-paste these " +
            "into your E-Mail.";

    // The default sizes of the Window.
    final private static int WINDOW_WIDTH = 500;
    final private static int WINDOW_HEIGHT = 400;

    // The margins for the content area.
    final private static Insets CONTENT_MARGINS = new Insets(10,10,10,10);

    // The main content area.
    private VBox contentArea;

    // The Inventory to get data from.
    private Inventory inventory;

    /**
     * Creates a Dialog with the specified Window name. Note that immediately after this, one must call createAndShow().
     * @param windowName The name of the Window.
     * @param inventory The Inventory to get data from.
     */
    public RequestsDialog(String windowName, Inventory inventory) {
        super(windowName);
        this.inventory = inventory;
        super.createAndShow();
        super.setWindowSize(RequestsDialog.WINDOW_WIDTH, RequestsDialog.WINDOW_HEIGHT);
    }

    @Override
    public Node createContentPane() {
        TextArea textArea = new TextArea();
        textArea.setText(this.getRequests());

        Label title = new Label(RequestsDialog.TITLE_TEXT);
        title.setWrapText(true);
        title.setTextFill(Color.WHITE);
        title.setMinHeight(50);

        this.contentArea = new VBox();
        this.contentArea.getChildren().add(title);
        this.contentArea.getChildren().add(textArea);
        this.contentArea.setPadding(RequestsDialog.CONTENT_MARGINS);
        return this.contentArea;
    }

    /**
     * Returns a String representation of all the Ingredients that need to be requested to display on the E-Mail.
     * @return The String representation of the Ingredients to request.
     */
    private String getRequests() {
        ArrayList<Ingredient> ingredients = this.inventory.getStock();
        String text = "";
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getAmount() <= ingredient.getRestockThreshold()) {
                text += ingredient.getName() + ": " + ingredient.getRequestAmount();
                text += "\n";
            }
        }
        return text;
    }

    @Override
    public void processInfo() {
        return;
    }
}