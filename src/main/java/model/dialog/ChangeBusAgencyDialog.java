package model.dialog;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.dialog.result.ChangeBusAgencyDialogResult;
import model.dialog.result.FinishHireDialogResult;
import model.mapping.*;
import model.repository.AgencyRepository;
import model.repository.DistrictRepository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ChangeBusAgencyDialog {
    private ButtonType doneButton;
    private Node doneButtonNode;
    private Dialog<ChangeBusAgencyDialogResult> dialog;
    private GridPane grid;
    private Label currentDistrict;
    private Label currentAgencyId;
    private ChoiceBox<String> newDistrictBox;
    private ChoiceBox<Long> newAgencyBox;
    private Agency agencyOfBus;
    private AgencyRepository agencyRepository;
    private DistrictRepository districtRepository;

    public ChangeBusAgencyDialog(Agency agencyOfBus, AgencyRepository agencyRepository, DistrictRepository districtRepository)
    {
        this.agencyOfBus = agencyOfBus;
        this.agencyRepository = agencyRepository;
        this.districtRepository = districtRepository;
    }

    public Optional<ChangeBusAgencyDialogResult> showAndWait()
    {
        createDialog();
        setButtons();
        createGrid();
        createFieldAndBoxes();
        fillGridWithBoxesAndLabels();

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == doneButton) {

                return ChangeBusAgencyDialogResult.builder()
                                    .DistrictName(newDistrictBox.getValue())
                                    .AgencyId(newAgencyBox.getValue())
                                    .build();

            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void createDialog()
    {
        dialog = new Dialog<>();
        dialog.setTitle("Change Bus Agency");
        dialog.setHeaderText("Pick new District and Agency");
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
        currentDistrict = new Label();
        currentDistrict.setText(agencyOfBus.getAgencyPk().getDistrict().getName());

        currentAgencyId = new Label();
        currentAgencyId.setText(agencyOfBus.getAgencyPk().getId().toString());

        newDistrictBox = new ChoiceBox<>();
        newDistrictBox.setOnAction(e -> districtBoxChanged());

        newAgencyBox = new ChoiceBox<>();
        newAgencyBox.setOnAction(e -> refreshDoneButton());

        List<String> districtNames = districtRepository.getAllNames();
        newDistrictBox.getItems().addAll(districtNames);
    }

    private void districtBoxChanged() {
        newAgencyBox.getItems().clear();

        District district = districtRepository.getByName(newDistrictBox.getSelectionModel().getSelectedItem());
        List<Agency> agencies = agencyRepository.getByDistrict(district);
        List<Long> agenciesIds = agencies.stream()
                .map(Agency::getAgencyPk)
                .map(AgencyPk::getId)
                .collect(Collectors.toList());

        newAgencyBox.getItems().addAll(agenciesIds);
    }

    private void refreshDoneButton() {
        doneButtonNode.setDisable(newAgencyBox.getValue() == null);
    }

    private void fillGridWithBoxesAndLabels()
    {
        grid.add(new Label("Current District of bus:"), 0, 0);
        grid.add(currentDistrict, 1, 0);

        grid.add(new Label("Current Agency of bus:"), 0, 1);
        grid.add(currentAgencyId, 1, 1);

        grid.add(new Label("Pick new district:"), 0, 2);
        grid.add(newDistrictBox, 1, 2);

        grid.add(new Label("Pick new agency:"), 0, 3);
        grid.add(newAgencyBox, 1, 3);
    }
}
