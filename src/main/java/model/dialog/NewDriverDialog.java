package model.dialog;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.dialog.result.NewAgencyDialogResult;
import model.dialog.result.NewBusDialogResult;
import model.mapping.Agency;
import model.mapping.Bus;
import model.mapping.District;
import model.repository.AgencyRepository;
import model.repository.BusRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class NewDriverDialog {
    private ButtonType doneButton;
    private Node doneButtonNode;
    private Dialog<NewBusDialogResult> dialog;
    private GridPane grid;
    private TextField registrationNumber, sizeCapacity, ocAc;
    private DatePicker techEvaluationDate;
    private BusRepository busRepository;
    private LocalDate date;

    public NewDriverDialog(BusRepository busRepository)
    {
        this.busRepository = busRepository;
    }

    public Optional<NewBusDialogResult> showAndWait()
    {
        createDialog();
        setButtons();
        createGrid();
        createFieldAndBoxes();
        fillGridWithBoxesAndLabels();

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == doneButton) {

                List<Bus> allBuses = busRepository.getAll();

                for(Bus b : allBuses){
                    if(b.getRegistrationNumber().equals(registrationNumber.getText())) {
                        new SimpleError("Can not add Bus", "Registration number is occupied!");
                        return null;
                    }
                }

                return NewBusDialogResult.builder()
                        .registrationNumber(registrationNumber.getText())
                        .techEvaluationDate(Date.valueOf(date))
                        .sizeCapacity(Long.parseLong(sizeCapacity.getText()))
                        .ocAc(Long.parseLong(ocAc.getText()))
                        .build();

            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void createDialog()
    {
        dialog = new Dialog<>();
        dialog.setTitle("New Bus");
        dialog.setHeaderText("Put info about bus");
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
        sizeCapacity = new TextField();
        sizeCapacity.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")){
                sizeCapacity.setText(newValue.replaceAll("[^\\d]", ""));
            }
            refreshDoneButton();
        });

        ocAc = new TextField();
        ocAc.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")){
                ocAc.setText(newValue.replaceAll("[^\\d]", ""));
            }
            refreshDoneButton();
        });

        techEvaluationDate = new DatePicker();
        techEvaluationDate.setOnAction(event -> {
            date = techEvaluationDate.getValue();
            refreshDoneButton();
        });

        registrationNumber = new TextField();
        registrationNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            refreshDoneButton();
        });
    }

    private void refreshDoneButton() {
        doneButtonNode.setDisable(registrationNumber.getText().isEmpty() || date == null ||
                ocAc.getText().isEmpty() || sizeCapacity.getText().isEmpty());
    }

    private void fillGridWithBoxesAndLabels()
    {
        grid.add(new Label("Registration Number:"), 0, 0);
        grid.add(registrationNumber, 1, 0);

        grid.add(new Label("Evaluation Date:"), 0, 1);
        grid.add(techEvaluationDate, 1, 1);

        grid.add(new Label("Oc / Ac:"), 0, 2);
        grid.add(ocAc, 1, 2);

        grid.add(new Label("Capacity of bus:"), 0, 3);
        grid.add(sizeCapacity, 1, 3);
    }
}
