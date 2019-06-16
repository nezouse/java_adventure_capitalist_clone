package controller;

import database.entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class ManagersController {
    private final User user;
    @FXML private FlowPane managersFlowPane;

    public ManagersController(User user) {
        this.user = user;
    }

    @FXML
    public void initialize() {
        user.breakLoopProperty().addListener((obs, oldStatus, newStatus) -> {
            if (newStatus) {
                managersFlowPane.getChildren().removeAll(managersFlowPane.getChildren());
                addManagersPanes();
            }
        });
        addManagersPanes();
    }

    private void addManagersPanes() {
        for (int i = 0; i < 10; i++) {
            if (!user.isManagerPresent(i)) {
                try {
                    createNewFlowPane(i);
                } catch (IOException e) {
                    System.out.println("Flowpane creation error");
                }
            }
        }
    }

    private void createNewFlowPane(int i) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/managerCell.fxml"));
        fxmlLoader.setController(new ManagerSingleCellController(user, i));
        GridPane newGridElement = fxmlLoader.load();
        managersFlowPane.getChildren().add(newGridElement);
    }
}
