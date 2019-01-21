package controller.user;

import com.google.common.hash.Hashing;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Data;
import model.dialog.RegistrationDialog;
import model.dialog.SimpleError;
import model.dialog.SimpleSucces;
import model.dialog.result.RegistrationDialogResult;
import model.mapping.User;
import model.repository.UserRepository;
import org.hibernate.Session;

import java.nio.charset.StandardCharsets;

@Data
public class UserLoginController {

    private Session session;
    private UserRepository userRepository;

    @FXML TextField usernameField;
    @FXML PasswordField passwordField;
    @FXML Button loginButton;
    @FXML Label wrongUsernameLabel, wrongPasswordLabel;

    @FXML
    private void initialize() {
        Platform.runLater(() -> userRepository = new UserRepository(session));
    }

    @FXML
    private void fieldChanged() {
        loginButton.setDisable(usernameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty());
    }

    @FXML
    private void loginButtonClicked(ActionEvent event){
        User user = userRepository.getByUsername(usernameField.getText());

        if(user == null){
            wrongUsernameLabel.setVisible(true);
            wrongPasswordLabel.setVisible(false);
            return;
        }

        String hashedPass = Hashing.sha256()
                .hashString( passwordField.getText(), StandardCharsets.UTF_8)
                .toString();

        if(user.getPassword().equals(hashedPass)){
            userLoggedIn(user, event);
        }else{
            wrongUsernameLabel.setVisible(false);
            wrongPasswordLabel.setVisible(true);
        }
    }

    @FXML
    private void registerButtonClicked(){
      RegistrationDialogResult newUserRequest = new RegistrationDialog().showAndWait().orElse(null);

        if(newUserRequest == null) return;

        if(userRepository.getByUsername(newUserRequest.getUsername()) != null)
        {
            new SimpleError("Registration Unsuccessful", "Username is already taken!");
            return;
        }

        String hashedPass = Hashing.sha256()
                .hashString( newUserRequest.getPassword(), StandardCharsets.UTF_8)
                .toString();

        User newUser = User.builder()
                            .username(newUserRequest.getUsername())
                            .password(hashedPass)
                            .phoneNumber(newUserRequest.getPhoneNumber())
                            .status(true)
                            .hireStatus(false)
                            .build();

        userRepository.save(newUser);
        new SimpleSucces("Registration Successful", "Your Username: " + newUser.getUsername());
    }

    private void userLoggedIn(User user, ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/user/userMainView.fxml"));
            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root);
           // scene.getStylesheets().add(getClass().getResource("/view/user/user.css").toExternalForm());
            Stage userMainStage = (Stage)((Node) event.getSource()).getScene().getWindow();
            UserMainController userMainController = fxmlLoader.getController();
            userMainController.setSession(session);
            userMainController.setLoggedUser(user);
            userMainStage.setScene(scene);
            userMainStage.setOnHiding( e -> {
                session.close();
                session.getSessionFactory().close();
            });
            userMainStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
