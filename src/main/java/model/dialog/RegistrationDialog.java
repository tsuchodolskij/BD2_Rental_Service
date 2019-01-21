package model.dialog;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.dialog.result.RegistrationDialogResult;

import java.util.Optional;

public class RegistrationDialog {

    private ButtonType registerButton;
    private Dialog<RegistrationDialogResult> dialog;
    private GridPane grid;
    private TextField usernameField;
    private TextField phoneField;
    private PasswordField passwordField;
    private Node registerButtonNode;

    public Optional<RegistrationDialogResult> showAndWait() {
        createDialog();
        setButtons();
        createGrid();
        createContent();
        fillGridWithBoxesAndLabels();

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == registerButton) {
                return RegistrationDialogResult.builder()
                                            .username(usernameField.getText())
                                            .password(passwordField.getText())
                                            .phoneNumber(phoneField.getText())
                                            .build();
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void createDialog() {
        dialog = new Dialog<>();
        dialog.setTitle("Registration");
        dialog.setHeaderText("Enter Your username and password");
    }

    private void setButtons() {
        registerButton = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButton, ButtonType.CANCEL);

        registerButtonNode = dialog.getDialogPane().lookupButton(registerButton);
        registerButtonNode.setDisable(true);
    }

    private void createGrid() {
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));
    }

    private void createContent() {
        usernameField = new TextField();
        usernameField.setPromptText("username");
        usernameField.textProperty().addListener(e -> refreshRegisterButton());

        passwordField = new PasswordField();
        passwordField.setPromptText("password");
        passwordField.textProperty().addListener(e -> refreshRegisterButton());

        phoneField = new TextField();
        phoneField.setPromptText("123123123");
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")){
                phoneField.setText(newValue.replaceAll("[^\\d]", ""));
            } else if(newValue.length() > 9){
                phoneField.setText(oldValue);
            }
            refreshRegisterButton();
        });
    }

    private void fillGridWithBoxesAndLabels() {
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Label phoneLabel = new Label("Phone number:");

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        GridPane.setHalignment(usernameLabel, HPos.RIGHT);

        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        GridPane.setHalignment(passwordLabel, HPos.RIGHT);

        grid.add(phoneLabel, 0, 2);
        grid.add(phoneField, 1, 2);
        GridPane.setHalignment(phoneLabel, HPos.RIGHT);
    }

    private void refreshRegisterButton(){
        registerButtonNode.setDisable( usernameField.getText().trim().isEmpty()
                                       || passwordField.getText().trim().isEmpty()
                                       || phoneField.getText().length() != 9);
    }

}