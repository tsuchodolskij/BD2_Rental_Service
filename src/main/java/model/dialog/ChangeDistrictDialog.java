package model.dialog;


import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Optional;

public class ChangeDistrictDialog {

    private ButtonType doneButton;
    private Dialog<String> dialog;
    private GridPane grid;
    private ChoiceBox<String> namesBox;
    private List<String> names;

    public ChangeDistrictDialog(List<String> names){
        this.names = names;
    }

    public Optional<String> showAndWait()
    {
        createDialog();
        setButtons();
        createGrid();
        createChoiceBoxes();
        fillGridWithBoxesAndLabels();

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == doneButton) {
                return namesBox.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void createDialog()
    {
        dialog = new Dialog<>();
        dialog.setTitle("New Hire");
        dialog.setHeaderText("Choose vehicle you want to hire");
    }

    private void setButtons()
    {
        doneButton = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(doneButton, ButtonType.CANCEL);
    }

    private void createGrid()
    {
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));
    }

    private void createChoiceBoxes()
    {
        namesBox = new ChoiceBox<>();
        namesBox.getItems().addAll(names);
        namesBox.getSelectionModel().select(0);
    }

    private void fillGridWithBoxesAndLabels()
    {
        grid.add(new Label("Vehicle ID:"), 0, 0);
        grid.add(namesBox, 1, 0);
    }
}
