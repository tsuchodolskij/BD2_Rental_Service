package controller.management;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lombok.Data;
import model.dialog.*;
import model.dialog.result.ChangeBusAgencyDialogResult;
import model.dialog.result.NewAgencyDialogResult;
import model.dialog.result.NewBusDialogResult;
import model.dialog.result.NewDriverDialogResult;
import model.mapping.*;
import model.repository.*;
import model.tableview.AgenciesInDistrict;
import model.tableview.AllBuses;
import model.tableview.AllDrivers;
import org.hibernate.Session;
import java.sql.Date;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class DriversController {

    private Session session;

    private AgencyRepository agencyRepository;
    private DistrictRepository districtRepository;
    private BusRepository busRepository;
    private DriverRepository driverRepository;

    @FXML TableView<AllDrivers> driversTable;
    @FXML TableColumn<AllDrivers, String> driversLicenceColumn, familyNameColumn, busRegistrationNumberColumn, districtNameColumn;
    @FXML AnchorPane loader;

    private AllDrivers currentRow;

    @FXML
    private void initialize() {
        configurateDriversTable();

        Platform.runLater(() ->{
            agencyRepository = new AgencyRepository(session);
            districtRepository = new DistrictRepository(session);
            busRepository = new BusRepository(session);
            loadDrivers();
        });
    }

    @FXML
    private void changeClicked(){
        refresh();

        if(currentRow == null)
        {
            new SimpleError("Can not change", "Row is not picked!");
            return;
        }

        Driver driverPicked = driverRepository.getByDriversLicenseID(currentRow.getDriversLicenseID());
        Agency agencyOfBus = busPicked.getAgency();

        /*List<Bus> allBuses = busRepository.getAll();
        Bus busPicked = allBuses.get(0);
        Agency agencyOfBus = busPicked.getAgency();

        for(Bus b : allBuses){
            if(b.getRegistrationNumber().equals(currentRow.getRegistration())) {
                busPicked = b;
                agencyOfBus = b.getAgency();
                break;
            }
        }*/

        ChangeBusAgencyDialogResult changeBus = new ChangeBusAgencyDialog(agencyOfBus, agencyRepository,
                districtRepository).showAndWait().orElse(null);

        if(changeBus == null) return;

        Agency ag = agencyRepository.getByPk(changeBus.getAgencyId(),
                districtRepository.getByName(changeBus.getDistrictName()));

        busPicked.setAgency(ag);
        busRepository.update(busPicked);

        loadDrivers();
    }

    @FXML
    private void newClicked(){
        refresh();

        NewDriverDialogResult newDriverResult = new NewDriverDialog(driverRepository).showAndWait().orElse(null);

        if(newDriverResult == null) return;

        Driver newDriver = Driver.builder()
                .driverLicenceId(newDriverResult.getDriverLicenceId())
                .familyName(newDriverResult.getFamilyName())
                .workStatus(newDriverResult.get())
                .ocAc(newDriverResult.getOcAc())
                .agency(agencyRepository.getAll().get(0))
                .drivers(null)
                .build();

        busRepository.save(newBus);
        loadDrivers();
    }

    @FXML
    private void tableClicked(){
        currentRow = driversTable.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void deleteClicked(){
        refresh();

        if(currentRow == null)
        {
            new SimpleError("Can not delete", "Row is not picked!");
            return;
        }

        Driver driverPicked = driverRepository.getByDriversLicenseID(currentRow.getDriversLicenseID());
        driverRepository.delete(driverPicked);

        /*List<Bus> allBuses = busRepository.getAll();

        for(Bus b : allBuses){

            if(b.getRegistrationNumber().equals(currentRow.getRegistration())) {
                busRepository.delete(b);
                break;
            }
        }*/

        loadDrivers();
    }

    private void loadDrivers(){
        refresh();

        new Thread(() -> {
            driversTable.setVisible(false);
            loader.setVisible(true);

            List<AllDrivers> driversList = driverRepository.getAll()
                    .stream()
                    .map(Driver::toAllDrivers)
                    .collect(Collectors.toList());

            driversTable.getItems().clear();
            driversTable.getItems().addAll(driversList);

            loader.setVisible(false);
            driversTable.setVisible(true);
        }).start();
    }

    private void refresh(){
        //session.refresh(driver);
        session.clear();
    }

    private void configurateDriversTable() {
        driversLicenceColumn.setCellValueFactory(new PropertyValueFactory<>("driversLicenseID"));
        familyNameColumn.setCellValueFactory(new PropertyValueFactory<>("familyName"));
        busRegistrationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("busRegistrationNumber"));
        districtNameColumn.setCellValueFactory(new PropertyValueFactory<>("districtName"));
    }
}
