package controller;

import database.entity.User;
import helper.Settings;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import task.WorkTask;

import java.util.stream.IntStream;

public class GridElementController {
    private final User user;
    private int position;
    @FXML private ProgressBar progressBar;
    @FXML private Button scoreButton;
    @FXML private Label levelLabel;
    @FXML private Button buyButton;
    @FXML private Label timeLabel;
    @FXML private ImageView image;
    @FXML private Text profit;

    /**
     * Basic constructor for GridElementController.
     *
     * @param user     user model
     * @param position position of element from 0 to 9
     */
    public GridElementController(User user, int position) {
        this.user = user;
        this.position = position;
    }

    /**
     * Setup all FXML elements and start a task if manager is present.
     */
    @FXML
    public void initialize() {
        setup();
        if (user.isManagerPresent(position)) {
            startTask(user, Settings.time[position]);
        }
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
     * Calculate the next level upgrade cost.
     *
     * @return Upgrade cost in decimal or scientific notation depending on length.
     */
    private String getCost() {
        double cost = Settings.costs[position] * Math.pow(1.07, (double) user.getLevel(position));
        return "Buy for " + getScientific(cost);
    }

    /**
     * Calculate the profit of the business.
     *
     * @return Upgrade profit in decimal or scientific notation depending on length.
     */
    private String getProfit() {
        double profit = Settings.money[position] * Math.pow(1.07, user.getLevel(position) - 1);
        return getScientific(profit);
    }

    /**
     * Convert time in seconds to h:m:s format
     *
     * @param time time in seconds
     * @return time in h:m:s format
     */
    private String getTime(long time) {
        int hours = (int) time / 3600;
        int remainder = (int) time - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        return String.format("%d:%d:%d", hours, mins, secs);
    }

    /**
     * Setup for all FXML elements(text, images and listeners).
     */
    private void setup() {

        levelLabel.setText(Integer.toString(user.getLevel(position)));
        buyButton.setText(getCost());
        image.setImage(new Image(getClass().getResourceAsStream(String.format("/icons/icon%d.png", position))));
        if (user.getLevel(position) == 0) {
            scoreButton.setDisable(true);
        }
        scoreButton.setOnAction(event -> startTask(user, Settings.time[position]));
        buyButton.setOnAction(event -> buyNextLevel(5));
        if (user.getLevel(position) != 0) profit.setText(getProfit());
        user.levelProperty(position).addListener((obs, oldStatus, newStatus) -> {
            Platform.runLater(() -> levelLabel.setText(newStatus.toString()));
            Platform.runLater(() -> profit.setText(getProfit()));
        });

    }

    /**
     * Check if user have enough points to buy a new level, and decrease task time if it's proper level.
     *
     * @param cost cost of upgrade
     */
    private void buyNextLevel(int cost) {
        if (user.getScore() >= cost) {
            if (user.getLevel(position) == 0) {
                scoreButton.setDisable(false);
            }
            user.setLevel(position, user.getLevel(position) + 1);
            buyButton.setText(getCost());
            synchronized (user) {
                user.substractFromScore(cost);
            }
            if (IntStream.of(Settings.timeDecrease).anyMatch(x -> x == user.getLevel(position))) {
                user.reduceTimer(position);
            }
        }
    }

    /**
     * Initialize and start the task.
     *
     * @param user         user model
     * @param timeToFinish time to complete the task
     */
    private void startTask(User user, int timeToFinish) {
        WorkTask task = new WorkTask(user, scoreButton, timeToFinish, position);
        task.timeProperty().addListener((obs, oldStatus, newStatus) ->
                Platform.runLater(() -> timeLabel.setText(getTime(newStatus.longValue()))));
        progressBar.progressProperty().bind(task.progressProperty());
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
