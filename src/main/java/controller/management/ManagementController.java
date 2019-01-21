package controller.management;

import controller.user.UserMainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Data;
import model.mapping.User;
import org.hibernate.Session;

@Data
public class ManagementController {

    private Session session;

    @FXML Button agencies;
    @FXML Button buses;
    @FXML private AnchorPane ap;

    @FXML private void initialize() {}

    @FXML
    private void driversClicked(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/management/drivers.fxml"));
            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage drivers = new Stage();
            drivers.initModality(Modality.WINDOW_MODAL);
            drivers.initOwner(ap.getScene().getWindow());
            drivers.setScene(scene);
            AgenciesController agenciesController = fxmlLoader.getController();
            agenciesController.setSession(session);
            drivers.setResizable(false);
            drivers.show();

            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            drivers.setX((primScreenBounds.getWidth()/2 - drivers.getWidth()) / 2);
            drivers.setY((primScreenBounds.getHeight() - drivers.getHeight()) / 2);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void agenciesClicked(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/management/agencies.fxml"));
            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage agencies = new Stage();
            agencies.initModality(Modality.WINDOW_MODAL);
            agencies.initOwner(ap.getScene().getWindow());
            agencies.setScene(scene);
            AgenciesController agenciesController = fxmlLoader.getController();
            agenciesController.setSession(session);
            agencies.setResizable(false);
            agencies.show();

            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            agencies.setX((primScreenBounds.getWidth()/2 - agencies.getWidth()) / 2);
            agencies.setY((primScreenBounds.getHeight() - agencies.getHeight()) / 2);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void busesClicked(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/management/buses.fxml"));
            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage buses = new Stage();
            buses.initModality(Modality.WINDOW_MODAL);
            buses.initOwner(ap.getScene().getWindow());
            buses.setScene(scene);
            BusesController busesController = fxmlLoader.getController();
            busesController.setSession(session);
            buses.setResizable(false);
            buses.show();

            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            buses.setX((primScreenBounds.getWidth()/2 - buses.getWidth()) / 2);
            buses.setY((primScreenBounds.getHeight() - buses.getHeight()) / 2);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
