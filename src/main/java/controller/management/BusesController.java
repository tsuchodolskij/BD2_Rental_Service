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
import model.mapping.*;
import model.repository.AddressRepository;
import model.repository.AgencyRepository;
import model.repository.BusRepository;
import model.repository.DistrictRepository;
import model.tableview.AgenciesInDistrict;
import model.tableview.AllBuses;
import org.hibernate.Session;
import java.sql.Date;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class BusesController {

    private Session session;

    private AgencyRepository agencyRepository;
    private DistrictRepository districtRepository;
    private BusRepository busRepository;

    @FXML TableView<AllBuses> busesTable;
    @FXML TableColumn<AllBuses, String> registrationColumn, districtColumn;
    @FXML TableColumn<AllBuses, Date> evaluationDateColumn;
    @FXML TableColumn<AllBuses, Long> capacityColumn;
    @FXML AnchorPane loader;

    private AllBuses currentRow;

    @FXML
    private void initialize() {
        configurateBusesTable();

        Platform.runLater(() ->{
            agencyRepository = new AgencyRepository(session);
            districtRepository = new DistrictRepository(session);
            busRepository = new BusRepository(session);
            loadBuses();
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

        Bus busPicked = busRepository.getByRegistration(currentRow.getRegistration());
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

        loadBuses();
    }

    @FXML
    private void newClicked(){
        refresh();

        NewBusDialogResult newBusResult = new NewBusDialog(busRepository).showAndWait().orElse(null);

        if(newBusResult == null) return;

        Bus newBus = Bus.builder()
                .registrationNumber(newBusResult.getRegistrationNumber())
                .techEvaluationDate(newBusResult.getTechEvaluationDate())
                .sizeCapacity(newBusResult.getSizeCapacity())
                .ocAc(newBusResult.getOcAc())
                .agency(agencyRepository.getAll().get(0))
                .drivers(null)
                .build();

        busRepository.save(newBus);
        loadBuses();
    }

    @FXML
    private void tableClicked(){
        currentRow = busesTable.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void deleteClicked(){
        refresh();

        if(currentRow == null)
        {
            new SimpleError("Can not delete", "Row is not picked!");
            return;
        }

        Bus busPicked = busRepository.getByRegistration(currentRow.getRegistration());
        busRepository.delete(busPicked);

        /*List<Bus> allBuses = busRepository.getAll();

        for(Bus b : allBuses){

            if(b.getRegistrationNumber().equals(currentRow.getRegistration())) {
                busRepository.delete(b);
                break;
            }
        }*/

        loadBuses();
    }

    private void loadBuses(){
        refresh();

        new Thread(() -> {
            busesTable.setVisible(false);
            loader.setVisible(true);

            List<AllBuses> busesList = busRepository.getAll()
                    .stream()
                    .map(Bus::toAllBuses)
                    .collect(Collectors.toList());

            busesTable.getItems().clear();
            busesTable.getItems().addAll(busesList);

            loader.setVisible(false);
            busesTable.setVisible(true);
        }).start();
    }

    private void refresh(){
        //session.refresh(bus);
        session.clear();
    }

    private void configurateBusesTable() {
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        evaluationDateColumn.setCellValueFactory(new PropertyValueFactory<>("evaluationDate"));
        districtColumn.setCellValueFactory(new PropertyValueFactory<>("district"));
        registrationColumn.setCellValueFactory(new PropertyValueFactory<>("registration"));
    }
}
