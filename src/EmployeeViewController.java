import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class EmployeeViewController is the Controller class of the main page that gives access to Server, Chef, Manager and
 * the tables scheme.
 */
public class EmployeeViewController {

    /**
     * Opens ServerGUI.fxml to load the scene for the Server.
     * @throws IOException In case the fxml file is corrupted.
     */
    public void openServer() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("ServerGUI.fxml"));
        stage.setTitle("Server");
        stage.setScene(new Scene(root, 800, 550));
        stage.show();
    }
    /**
     * Opens ChefGUI.fxml to load the scene for the Server.
     * @throws IOException In case the fxml file is corrupted.
     */
    public void openChef() throws IOException{
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("ChefGUI.fxml"));
        stage.setTitle("Chef");
        stage.setScene(new Scene(root, 600, 400));

        stage.show();
    }

    /**
     * This function gets the Manager's scene and sets it.
     */
    public void openManager(){
        Stage stage = new Stage();
        ManagerController manager = new ManagerController();
        Scene scene = manager.getScene();
        stage.setTitle("Manager");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Opens TablesView.fxml to load the scene for the Server.
     * @throws IOException In case the fxml file is corrupted.
     */
    public void openTables() throws IOException{
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("TablesView.fxml"));
        stage.setTitle("Tables");
        stage.setScene(new Scene(root, 900, 400));

        stage.show();
    }

    /**
     * Opens ServerGUI.fxml to load the scene for the Server.
     * @throws IOException In case the fxml file is corrupted.
     */
    public void openReceiver() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("ReceiverView.fxml"));
        stage.setTitle("Receiver");
        stage.setScene(new Scene(root, 350, 500));
        stage.show();
    }
}
