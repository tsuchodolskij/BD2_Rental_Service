package model.dialog;

import javafx.scene.control.Alert;

public class SimpleSucces {
    public SimpleSucces(String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Succes");
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
