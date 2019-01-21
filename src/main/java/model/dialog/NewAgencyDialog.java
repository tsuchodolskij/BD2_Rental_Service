package model.dialog;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.dialog.result.NewAgencyDialogResult;
import model.mapping.*;
import model.repository.AgencyRepository;

import java.util.List;
import java.util.Optional;

public class NewAgencyDialog {
    private ButtonType doneButton;
    private Node doneButtonNode;
    private Dialog<NewAgencyDialogResult> dialog;
    private GridPane grid;
    private TextField parkingPlaces, code, house, street;
    private District district;
    private AgencyRepository agencyRepository;

    public NewAgencyDialog(District district, AgencyRepository agencyRepository)
    {
        this.district = district;
        this.agencyRepository = agencyRepository;
    }

    public Optional<NewAgencyDialogResult> showAndWait()
    {
        createDialog();
        setButtons();
        createGrid();
        createFieldAndBoxes();
        fillGridWithBoxesAndLabels();

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == doneButton) {

                /*List<Agency> allAgencies = agencyRepository.getAll();

                for(Agency a : allAgencies){
                    if(a.getAddress().getAddressPk().getHouseNumber().equals(house.getText()) &&
                            a.getAddress().getAddressPk().getPostCode().equals(code.getText())  &&
                            a.getAddress().getAddressPk().getStreet().equals("ul. " + street.getText())) {

                        new SimpleError("Can not add Agency", "Address is occupied!");
                        return null;
                    }
                }*/

                if(agencyRepository.getByAddress(house.getText(), code.getText(), "ul. " + street.getText()) != null) {

                    new SimpleError("Can not add Agency", "Address is occupied!");
                    return null;
                }

                return NewAgencyDialogResult.builder()
                        .code(code.getText())
                        .house(house.getText())
                        .street("ul. " + street.getText())
                        .parkingPlaces(Long.parseLong(parkingPlaces.getText()))
                        .build();

            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void createDialog()
    {
        dialog = new Dialog<>();
        dialog.setTitle("New Agency");
        dialog.setHeaderText("Adding new agency in: " + district.getName());
    }

    private void setButtons()
    {
        doneButton = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(doneButton, ButtonType.CANCEL);

        doneButtonNode = dialog.getDialogPane().lookupButton(doneButton);
        doneButtonNode.setDisable(true);
    }

    private void createGrid()
    {
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));
    }

    private void createFieldAndBoxes()
    {
        parkingPlaces = new TextField();
        parkingPlaces.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")){
                parkingPlaces.setText(newValue.replaceAll("[^\\d]", ""));
            }
            refreshDoneButton();
        });

        code = new TextField();
        code.textProperty().addListener((observable, oldValue, newValue) -> {
            refreshDoneButton();
        });
        house = new TextField();
        house.textProperty().addListener((observable, oldValue, newValue) -> {
            refreshDoneButton();
        });
        street = new TextField();
        street.textProperty().addListener((observable, oldValue, newValue) -> {
            refreshDoneButton();
        });
    }

    private void refreshDoneButton() {
        doneButtonNode.setDisable(street.getText().isEmpty() || parkingPlaces.getText().isEmpty() ||
                code.getText().isEmpty() || house.getText().isEmpty());
    }

    private void fillGridWithBoxesAndLabels()
    {
        grid.add(new Label("Street name:"), 0, 0);
        grid.add(street, 1, 0);

        grid.add(new Label("Post Code:"), 0, 1);
        grid.add(code, 1, 1);

        grid.add(new Label("House Number:"), 0, 2);
        grid.add(house, 1, 2);

        grid.add(new Label("Parking places:"), 0, 3);
        grid.add(parkingPlaces, 1, 3);
    }
}
