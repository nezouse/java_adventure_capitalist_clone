package controller;

import database.entity.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import task.AutosaveTask;

import java.io.IOException;


public class MainSceneController {
    @FXML private Label scoreLabel;
    @FXML private GridPane grid;
    @FXML private StackPane stackPane;
    @FXML private StackPane centerStack;
    @FXML private Button carrerButton;
    @FXML private Button managersButton;
    @FXML private Button investorsButton;

    private User user;

    @FXML
    public void initialize() throws Exception {
        Stage stage = new Stage();
        LoginController lc = new LoginController(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
        fxmlLoader.setController(lc);
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle("User selection");
        stage.showAndWait();

        user = User.find.byName(lc.getUsername());
        if (user != null) {
            user.initialize();
            user.save();
        } else {
            user = new User(lc.getUsername());
        }

        Thread autosave = new AutosaveTask(user);
        autosave.setDaemon(true);
        autosave.start();

        scoreLabel.setText(getScientific(user.getScore()));
        user.scoreProperty().addListener((obs, oldStatus, newStatus) ->
                Platform.runLater(() -> scoreLabel.setText(getScientific(user.getScore()))));

        createNewGridElement(0, 0, 0);
        createNewGridElement(0, 1, 1);
        createNewGridElement(0, 2, 2);
        createNewGridElement(0, 3, 3);
        createNewGridElement(0, 4, 4);
        createNewGridElement(1, 0, 5);
        createNewGridElement(1, 1, 6);
        createNewGridElement(1, 2, 7);
        createNewGridElement(1, 3, 8);
        createNewGridElement(1, 4, 9);

        addToStackPane("/views/managers.fxml", new ManagersController(user));
        addToStackPane("/views/investors.fxml", new InvestorsController(user));

        carrerButton.setOnAction(event -> switchTo("#carrer"));
        managersButton.setOnAction(event -> switchTo("#managers"));
        investorsButton.setOnAction(event -> switchTo("#investors"));
    }

    private void addToStackPane(String localization, Object controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(localization));
        fxmlLoader.setController(controller);
        Pane pane = fxmlLoader.load();
        pane.setVisible(false);
        centerStack.getChildren().add(pane);
    }

    private void switchTo(String element) {
        for (Node n : centerStack.getChildren()) {
            n.setVisible(false);
        }
        centerStack.lookup(element).setVisible(true);
    }

    private void createNewGridElement(int column, int row, int position) throws Exception {
        GridPane newGridElement = new GridPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/gridElement.fxml"));
        fxmlLoader.setRoot(newGridElement);
        fxmlLoader.setController(new GridElementController(user, position));
        fxmlLoader.load();
        grid.add(newGridElement, column, row);
    }

    public void saveGame() {
        user.save();
    }

    /**
     * Convert number to string.
     *
     * @param number Number to convert.
     * @return String with number in decimal or scientific notation.
     */
    private String getScientific(double number) {
        number = Math.round(number * 100.0) / 100.0;
        if (number < Math.pow(10, 6)) {
            return Double.toString(number);
        } else {
            return String.format("%.4E", number);
        }
    }
}
