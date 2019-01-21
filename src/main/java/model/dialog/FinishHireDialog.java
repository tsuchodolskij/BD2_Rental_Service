package model.dialog;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.dialog.result.FinishHireDialogResult;
import model.mapping.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class FinishHireDialog {
    private ButtonType doneButton;
    private Node doneButtonNode;
    private Dialog<FinishHireDialogResult> dialog;
    private GridPane grid;
    private TextField hireTime;
    private List<District> districts;
    private ChoiceBox<String> districtBox;
    private ChoiceBox<Long> stationBox;
    private ChoiceBox<Long> terminalBox;
    private String vehicleType;

    public FinishHireDialog(List<District> districts, String vehicleType)
    {
        this.districts = districts;
        this.vehicleType = vehicleType;
    }

    public Optional<FinishHireDialogResult> showAndWait()
    {
        createDialog();
        setButtons();
        createGrid();
        createFieldAndBoxes();
        fillGridWithBoxesAndLabels();

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == doneButton) {
                Timestamp finisDate = new Timestamp(new Date().getTime());
                finisDate.setTime(finisDate.getTime() + TimeUnit.MINUTES.toMillis(Integer.parseInt(hireTime.getText())));

                return FinishHireDialogResult.builder()
                                    .DistrictName(districtBox.getValue())
                                    .rentFinishDate(finisDate)
                                    .StationId(stationBox.getValue())
                                    .TerminalId(terminalBox.getValue())
                                    .build();

            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void createDialog()
    {
        dialog = new Dialog<>();
        dialog.setTitle("Finish Hire");
        dialog.setHeaderText("Simulation of user finishing his rent");
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
        hireTime = new TextField();
        hireTime.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")){
                hireTime.setText(newValue.replaceAll("[^\\d]", ""));

            }
            refreshDoneButton();
        });

        districtBox =  new ChoiceBox<>();
        districtBox.setOnAction(e -> districtBoxChanged());

        stationBox  = new ChoiceBox<>();
        stationBox.setOnAction(e -> stationBoxChanged());

        terminalBox  = new ChoiceBox<>();
        terminalBox.setOnAction(e -> refreshDoneButton());

        List<String> districtNames = districts.stream().map(District::getName).collect(Collectors.toList());
        districtBox.getItems().addAll(districtNames);
    }

    private void districtBoxChanged() {
        stationBox.getItems().clear();
        terminalBox.getItems().clear();

        District district = districts.get( districtBox.getSelectionModel().getSelectedIndex() );
        List<Long> stationIds = district.getStations().stream()
                                                  .map(Station::getStationPk)
                                                  .map(StationPk::getId)
                                                  .collect(Collectors.toList());

        stationBox.getItems().addAll(stationIds);
    }

    private void stationBoxChanged() {
        if(stationBox.getValue() == null) return;

        terminalBox.getItems().clear();

        District district = districts.get(districtBox.getSelectionModel().getSelectedIndex());
        Station station = district.getStations().get(stationBox.getSelectionModel().getSelectedIndex());

        List<Long> terminalIds = station.getTerminals().stream()
                                                    .filter(e -> e.getType().getName().equals(vehicleType))
                                                    .filter(e -> e.getVehicle() == null)
                                                    .map(Terminal::getTerminalPk)
                                                    .map(TerminalPk::getId)
                                                    .collect(Collectors.toList());


        terminalBox.getItems().addAll(terminalIds);
    }

    private void refreshDoneButton() {
        doneButtonNode.setDisable(terminalBox.getValue() == null || hireTime.getText().isEmpty());
    }

    private void fillGridWithBoxesAndLabels()
    {
        grid.add(new Label("Hire duration(minutes):"), 0, 0);
        grid.add(hireTime, 1, 0);

        grid.add(new Label("District Name:"), 0, 1);
        grid.add(districtBox, 1, 1);

        grid.add(new Label("Station ID:"), 0, 2);
        grid.add(stationBox, 1, 2);

        grid.add(new Label("Terminal ID:"), 0, 3);
        grid.add(terminalBox, 1, 3);
    }
}
