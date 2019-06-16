package controller;

import database.entity.User;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;


public class LoginController {
    private String username = "default";
    private Stage stage;
    @FXML private VBox usernamesBox;
    @FXML private Text selectedUsername;
    @FXML private Button saveNewUserButton;
    @FXML private TextField newUserInputBox;
    @FXML private Button startGameButton;

    public LoginController(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        ArrayList<User> users = new ArrayList<>(User.find.all());
        usernamesBox.setSpacing(5);
        for (User u : users) {
            usernamesBox.getChildren().add(new Button(u.getLogin()));
        }
        for (Node b : usernamesBox.getChildren()) {
            ((Button) b).setOnAction(event -> selectedUsername.setText(((Button) b).getText()));
        }
        saveNewUserButton.setOnAction(event -> selectedUsername.setText(newUserInputBox.getText()));
        startGameButton.setOnAction(event -> {
            username = selectedUsername.getText();
            stage.close();
        });
    }

    public String getUsername() {
        return username;
    }
}
