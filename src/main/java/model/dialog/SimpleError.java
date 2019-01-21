package model.dialog;

import javafx.scene.control.Alert;

public class SimpleError {

    public SimpleError(String header, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
