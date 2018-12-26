import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 * A dialog that prompts the user to sign in.
 */
public class ManagerSignInDialog extends Dialog {

    // The margins for the content area.
    final private static Insets CONTENT_MARGINS = new Insets(10,10,10,10);

    // The text to display.
    final private static String USER_PROMPT_TEXT = "You have to sign in before you can do anything. Select your name" +
            " in the drop down list to sign in.";

    /**
     * Creates a Dialog with the specified Window name. Note that immediately after this, one must call createAndShow().
     * @param windowName The name of the Window.
     */
    public ManagerSignInDialog(String windowName) {
        super(windowName);
        super.createAndShow();
    }

    @Override
    public Node createContentPane() {
        Label text = new Label(ManagerSignInDialog.USER_PROMPT_TEXT);
        text.setTextFill(Color.WHITE);
        text.setWrapText(true);
        BorderPane contents = new BorderPane();
        contents.setCenter(text);
        contents.setAlignment(text, Pos.CENTER);
        contents.setPadding(ManagerSignInDialog.CONTENT_MARGINS);
        return contents;
    }

    @Override
    public void processInfo() {
        return;
    }
}
