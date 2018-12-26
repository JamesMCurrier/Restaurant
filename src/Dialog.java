import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * An abstract class that represents a Dialog. A Dialog box consists of a 'OK' button at the bottom, which, when
 * pressed, closes the Dialog box. It also consists of a 'Content Pane' that stores the contents of the dialog box.
 * In order to use and create a Dialog, one must use one it it's children. The children should handle the creation of
 * the 'Content Pane' of the Dialog, as well as any potential listeners that might listen in on the buttons inside of
 * said 'Content Pane'.
 */
public abstract class Dialog {

    // Default window widths and heights.
    private static int DEFAULT_WINDOW_WIDTH = 400;
    private static int DEFAULT_WINDOW_HEIGHT = 250;

    // The stage and panes.
    private Stage dialogStage;
    // The main pane that includes the contents of the Dialog box and the close button.
    private BorderPane mainPane;
    // The center container that is the 'Content Pane' of the Dialog box.
    private Node contentPane;

    // The return button.
    private Button returnButton;
    // Text for the return button.
    final private static String RETURN_BUTTON_TEXT = "OK";
    // Width of the return button.
    final private static int RETURN_BUTTON_WIDTH = 75;
    // Padding for the button.
    final private static Insets RETURN_BUTTON_PADDING = new Insets(10,10,10,10);
    // The container at the bottom that contains the return button.
    private HBox bottomContainer;

    // The Style of the content area.
    final private static String DIALOG_STYLE = "-fx-background-color: #464646;";

    // The listener.
    private DialogCloseListener listener;

    /**
     * Creates a Dialog with the specified Window name. Note that immediately after this, one must call createAndShow().
     * @param windowName The name of the Window.
     */
    public Dialog(String windowName) {
        this.dialogStage = new Stage();
        this.dialogStage.setTitle(windowName);
        this.dialogStage.initModality(Modality.WINDOW_MODAL);
    }

    /**
     * Create and show this Dialog box. This must be called immediately after the call to super() in each child's
     * constructor.
     */
    public void createAndShow() {
        this.contentPane = this.createContentPane();
        this.createMainPane();

        // Set the scene.
        this.setScene(new Scene(this.mainPane));
        this.setWindowSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);

        // Disabled Dialog box resizing.
        this.dialogStage.setResizable(false);
        // Show the dialog and wait until the user closes it.
        this.dialogStage.show();
    }

    /**
     * Method that returns the contents of this Dialog box in the form of a Node. Must be instantiated by children
     * so that they can specify their unique contents.
     * @return The Node that represents the contents of the Dialog box.
     */
    public abstract Node createContentPane();

    /**
     * Close this Dialog. Alerts listeners.
     */
    private void closeWindow() {
        this.processInfo();
        if (this.listener != null) {
            this.listener.dialogClosed(this);
        }
        this.dialogStage.close();
    }

    /**
     * Process the information that was just entered by the user and act accordingly.
     */
    public abstract void processInfo();

    /**
     * Create the main Pane. Sets up the button and child panes that store the contents of the dialog box.
     */
    private void createMainPane() {
        this.mainPane =  new BorderPane();
        this.returnButton = new Button(Dialog.RETURN_BUTTON_TEXT);
        this.returnButton.setPrefWidth(RETURN_BUTTON_WIDTH);
        this.returnButton.setOnMouseClicked(e -> this.closeWindow());

        this.bottomContainer = new HBox();
        this.bottomContainer.getChildren().add(this.returnButton);
        this.bottomContainer.setMargin(this.returnButton, Dialog.RETURN_BUTTON_PADDING);
        this.bottomContainer.setAlignment(Pos.BOTTOM_RIGHT);
        this.mainPane.setBottom(bottomContainer);
        this.mainPane.setCenter(contentPane);
        this.mainPane.setAlignment(this.returnButton, Pos.BOTTOM_RIGHT);
        this.mainPane.setStyle(DIALOG_STYLE);
    }

    /**
     * Set the listener.
     * @param listener The listener.
     */
    public void setListener(DialogCloseListener listener) {
        this.listener = listener;
    }

    /**
     * Sets the size of the Window of this Dialog box.
     * @param newWidth The new width of the Window.
     * @param newHeight The new height of the Window.
     */
    public void setWindowSize(int newWidth, int newHeight) {
        this.dialogStage.setWidth(newWidth);
        this.dialogStage.setHeight(newHeight);
    }

    public Scene getScene() {
        return this.dialogStage.getScene();
    }

    public void setScene(Scene newScene) {
        this.dialogStage.setScene(newScene);
    }
}