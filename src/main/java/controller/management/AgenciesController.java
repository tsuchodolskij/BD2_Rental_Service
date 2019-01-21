package controller.management;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lombok.Data;
import model.dialog.ChangeDistrictDialog;
import model.dialog.NewAgencyDialog;
import model.dialog.SimpleError;
import model.dialog.result.NewAgencyDialogResult;
import model.mapping.*;
import model.repository.*;
import model.tableview.AgenciesInDistrict;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class AgenciesController {

    private Session session;
    private District district;

    private AgencyRepository agencyRepository;
    private DistrictRepository districtRepository;
    private AddressRepository addressRepository;

    @FXML TableView<AgenciesInDistrict> agenciesTable;
    @FXML TableColumn<AgenciesInDistrict, String> streetColumn, houseColumn, codeColumn;
    @FXML TableColumn<AgenciesInDistrict, Long> parkingPlacesColumn;
    @FXML AnchorPane loader;
    @FXML Label tableLabel;

    private AgenciesInDistrict currentRow;

    @FXML
    private void initialize() {
        configurateAgenciesTable();

        Platform.runLater(() ->{
            agencyRepository = new AgencyRepository(session);
            districtRepository = new DistrictRepository(session);
            addressRepository = new AddressRepository(session);
            district = districtRepository.getByName("Bemowo");
            tableLabel.setText("Agencies in chosen district: " + district.getName());
            loadAgencies();
        });
    }

    @FXML
    private void changeClicked(){
        refresh();

        Optional<String> result = new ChangeDistrictDialog(districtRepository.getAllNames()).showAndWait();
        if(!result.isPresent()) return;

        district = districtRepository.getByName(result.get());
        tableLabel.setText("Agencies in chosen district: " + district.getName());

        loadAgencies();
    }

    @FXML
    private void newClicked(){
        refresh();

        NewAgencyDialogResult newAgencyResult = new NewAgencyDialog(district, agencyRepository).showAndWait().orElse(null);

        if(newAgencyResult == null) return;

        AddressPk newAddressPk = AddressPk.builder()
                .street(newAgencyResult.getStreet())
                .houseNumber(newAgencyResult.getHouse())
                .postCode(newAgencyResult.getCode())
                .build();

        Address newAddress = Address.builder()
                .addressPk(newAddressPk)
                .station(null)
                .agency(null)
                .build();

        addressRepository.save(newAddress);

        AgencyPk newAgencyPk = AgencyPk.builder()
                .district(district)
                .build();

        Agency newAgency = Agency.builder()
                .agencyPk(newAgencyPk)
                .parkingPlacesCapacity(newAgencyResult.getParkingPlaces())
                .address(newAddress)
                .buses(null)
                .drivers(null)
                .build();

        agencyRepository.save(newAgency);
        loadAgencies();
    }

    @FXML
    private void tableClicked(){
        currentRow = agenciesTable.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void deleteClicked(){
        refresh();

        if(currentRow == null)
        {
            new SimpleError("Can not delete", "Row is not picked!");
            return;
        }

        /*List<Agency> allAgencies = agencyRepository.getAll();

        for(Agency a : allAgencies){

            if(a.getAddress().getAddressPk().getHouseNumber().equals(currentRow.getHouse()) &&
                    a.getAddress().getAddressPk().getPostCode().equals(currentRow.getCode())  &&
                    a.getAddress().getAddressPk().getStreet().equals(currentRow.getStreet())) {

                agencyRepository.delete(a);
                break;
            }
        }*/

        Agency toDelete = agencyRepository.getByAddress(currentRow.getHouse(), currentRow.getCode(),
                currentRow.getStreet());

        Address toDeleteWithAgency = toDelete.getAddress();

        agencyRepository.delete(toDelete);
        addressRepository.delete(toDeleteWithAgency);

        loadAgencies();
    }

    private void loadAgencies(){
        refresh();

        new Thread(() -> {
            agenciesTable.setVisible(false);
            loader.setVisible(true);

            List<AgenciesInDistrict> agenciesList = agencyRepository.getByDistrict(district)
                    .stream()
                    .map(Agency::toAgenciesInDistrict)
                    .collect(Collectors.toList());

            agenciesTable.getItems().clear();
            agenciesTable.getItems().addAll(agenciesList);

            loader.setVisible(false);
            agenciesTable.setVisible(true);
        }).start();
    }

    private void refresh(){
        session.refresh(district);
        session.clear();
    }

    private void configurateAgenciesTable() {
        streetColumn.setCellValueFactory(new PropertyValueFactory<>("street"));
        houseColumn.setCellValueFactory(new PropertyValueFactory<>("house"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        parkingPlacesColumn.setCellValueFactory(new PropertyValueFactory<>("parkingPlaces"));
    }
}
