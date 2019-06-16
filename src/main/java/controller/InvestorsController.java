package controller;

import database.entity.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class InvestorsController {
    private final User user;
    @FXML private Text totalAngels;
    @FXML private Text bonusAngels;
    @FXML private Text restartAngels;
    @FXML private Button claimAngels;

    public InvestorsController(User user) {
        this.user = user;
    }

    @FXML
    public void initialize() {
        totalAngels.setText(getScientific(user.getAngelInvestors()));
        bonusAngels.setText("2%");
        restartAngels.setText(getScientific(calculateAngels()));
        user.scoreProperty().addListener((obs, oldStatus, newStatus) ->
                Platform.runLater(() -> restartAngels.setText(getScientific(calculateAngels()))));
        claimAngels.setOnAction(event -> {
            user.claimAngels((int) calculateAngels());
        });
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

    /**
     * Calculate number of angel investors from user lifetime score.
     *
     * @return number of angel investors
     */
    private double calculateAngels() {
        return (int) (150 * Math.sqrt(user.getLifetimeScore() / Math.pow(10, 5)));
    }

}
