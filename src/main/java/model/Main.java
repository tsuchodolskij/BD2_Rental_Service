package model;

import controller.management.ManagementController;
import controller.user.UserLoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.mapping.Address;
import model.mapping.Agency;
import model.mapping.Bus;
import model.repository.AddressRepository;
import model.repository.AgencyRepository;
import model.repository.BusRepository;
import model.repository.DistrictRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main extends Application {

    private static Session userSession;
    private static Session managementSession;

    public static void main(String[] args) {
        createSessions();
        launch(args);

        /*AddressRepository add = new AddressRepository(managementSession);
        AgencyRepository ag = new AgencyRepository(managementSession);

        Agency toDelete = ag.getByAddress("1", "00-000", "c");

        Address toDeleteWithAgency = toDelete.getAddress();

        add.delete(toDeleteWithAgency);
        ag.delete(toDelete);

        userSession.close();
        userSession.getSessionFactory().close();

        managementSession.close();
        managementSession.getSessionFactory().close();*/
    }

    @Override
    public void start(Stage managementStage) {
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        Stage userStage = new Stage();

        loadStage(managementStage, "management/management.fxml", "Management Vehicle Rental", managementSession);
        managementStage.setOnHiding(e -> closeSession(managementSession));
        managementStage.show();
        managementStage.setX((primScreenBounds.getWidth()/2 - managementStage.getWidth()) / 2);
        managementStage.setY((primScreenBounds.getHeight() - managementStage.getHeight()) / 2);

        loadStage(userStage, "user/userLoginView.fxml", "User Vehicle Rental", userSession);
        userStage.setOnHiding(e -> closeSession(userSession));
        userStage.show();
        userStage.setX(managementStage.getX() + primScreenBounds.getWidth()/2);
        userStage.setY(managementStage.getY());
    }

    private void loadStage(Stage stage, String resourcePath, String title, Session session)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/" + resourcePath));
            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root);

            if (title.equals("User Vehicle Rental")) {
                UserLoginController userLoginController = fxmlLoader.getController();
                userLoginController.setSession(session);
            } else{
                ManagementController managementController = fxmlLoader.getController();
                managementController.setSession(session);
            }

            stage.setScene(scene);
            stage.setTitle(title);
            stage.setResizable(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createSessions()
    {
        Configuration configuration = new Configuration().configure("/hibernate.cfg.xml");
        SessionFactory userFactory = configuration.buildSessionFactory();
        SessionFactory managementFactory = configuration.buildSessionFactory();

        userSession = userFactory.openSession();
        managementSession = managementFactory.openSession();
    }

    private void closeSession(Session session)
    {
        session.close();
        session.getSessionFactory().close();
    }
}
