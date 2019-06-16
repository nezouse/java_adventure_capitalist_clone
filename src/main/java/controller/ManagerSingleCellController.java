package controller;

import database.entity.User;
import helper.Settings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ManagerSingleCellController {
    private final User user;
    @FXML private Text name;
    @FXML private Text description;
    @FXML private Text cost;
    @FXML private Button button;
    @FXML private GridPane wrapper;
    private int position;

    public ManagerSingleCellController(User u, int p) {
        user = u;
        position = p;
    }

    /**
     * Initializes FXML elements of single cell.
     */
    @FXML
    public void initialize() {
        name.setText(Settings.managerNames[position]);
        description.setText(Settings.managerDescriptions[position]);
        cost.setText("Cost: " + Settings.managerCosts[position]);
        button.setOnAction(event -> {
            user.setManager(position, true);
            ((Pane) wrapper.getParent()).getChildren().remove(wrapper);
        });
        if (user.isManagerPresent(position)) {
            button.setDisable(true);
        }
    }
}
