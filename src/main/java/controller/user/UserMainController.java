package controller.user;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;;
import lombok.Data;
import model.dialog.FinishHireDialog;
import model.dialog.SimpleError;
import model.dialog.NewHireDialog;
import model.dialog.result.FinishHireDialogResult;
import model.mapping.Hire;
import model.mapping.User;
import model.repository.DistrictRepository;
import model.repository.HireRepository;
import model.repository.UserRepository;
import model.repository.VehicleRepository;
import model.tableview.UserHireHistory;
import org.hibernate.Session;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class UserMainController {

    private Session session;
    private User loggedUser;

    private UserRepository userRepository;
    private HireRepository hireRepository;
    private VehicleRepository vehicleRepository;
    private DistrictRepository districtRepository;

    @FXML Label loggedUserLabel;
    @FXML TableView<UserHireHistory> historyTable;
    @FXML TableColumn<UserHireHistory, Timestamp> startDateColumn, finishDateColumn;
    @FXML TableColumn<UserHireHistory, String> vehicleTypeColumn;
    @FXML TableColumn<UserHireHistory, Long> hirePriceColumn;
    @FXML AnchorPane loader;

    @FXML
    private void initialize() {
        configurateHistoryTable();

        Platform.runLater(() ->{
            loggedUserLabel.setText(loggedUserLabel.getText() + loggedUser.getUsername());
            userRepository = new UserRepository(session);
            hireRepository = new HireRepository(session);
            vehicleRepository = new VehicleRepository(session);
            districtRepository = new DistrictRepository(session);
            loadHistory();
        });
    }

    @FXML
    public void logout(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/user/userLoginView.fxml"));
            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root);
            //scene.getStylesheets().add(getClass().getResource("/view/user/user.css").toExternalForm());
            Stage userLoginStage = (Stage)((Node) event.getSource()).getScene().getWindow();
            UserLoginController userLoginController = fxmlLoader.getController();
            userLoginController.setSession(session);
            userLoginStage.setScene(scene);
            userLoginStage.setOnHiding( e -> {
                session.close();
                session.getSessionFactory().close();
            });
            userLoginStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void newHireClicked(){
        refresh();

        if(loggedUser.isHireStatus()) {
            new SimpleError("Can't make new hire", "You already have not finished hire");
            return;
        }
        if(!loggedUser.isStatus()) {
             new SimpleError("Can't make new hire", "Your account has been disabled");
             return;
        }

        Optional<Long> result = new NewHireDialog(vehicleRepository.getNotOccupiedVehicleIds()).showAndWait();
        if(!result.isPresent()) return;

        Hire newHire = Hire.builder()
                                .rentStartDate(new Timestamp(new Date().getTime()))
                                .rentFinishDate(null)
                                .hirePrice(null)
                                .user(loggedUser)
                                .vehicle(vehicleRepository.getVehicleById(result.get()))
                                .build();

        hireRepository.save(newHire);
        loadHistory();
    }

    @FXML
    private void finishHireClicked(){
        refresh();

        if(!loggedUser.isHireStatus()) {
            new SimpleError("Can't finish hire", "You don't have any unfinished hires");
            return;
        }

        String unfishishedHireVehicleType = historyTable.getItems().stream()
                                                .filter(e -> e.getFinishDate()==null)
                                                .map(UserHireHistory::getVehicleType)
                                                .findFirst()
                                                .orElse(null);

        FinishHireDialogResult finishHireRequest = new FinishHireDialog(districtRepository.getAll(), unfishishedHireVehicleType).showAndWait().orElse(null);

        if(finishHireRequest == null) return;

        hireRepository.finishHire(finishHireRequest);
        loadHistory();
    }

    private void configurateHistoryTable() {
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        finishDateColumn.setCellValueFactory(new PropertyValueFactory<>("finishDate"));
        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        hirePriceColumn.setCellValueFactory(new PropertyValueFactory<>("hirePrice"));
    }

    private void loadHistory(){
        refresh();

        new Thread(() -> {
            historyTable.setVisible(false);
            loader.setVisible(true);

            List<UserHireHistory> userHireHistory = hireRepository.getByUser(loggedUser)
                    .stream()
                    .map(Hire::toUserHireHistory)
                    .collect(Collectors.toList());

            historyTable.getItems().clear();
            historyTable.getItems().addAll(userHireHistory);

            loader.setVisible(false);
            historyTable.setVisible(true);
        }).start();
    }

    private void refresh(){
        session.refresh(loggedUser);
        session.clear();
    }

}
