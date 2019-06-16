package task;

import database.entity.User;
import helper.Settings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.scene.control.Button;

/**
 * Thread which is responsible for waiting unless the task is finished.
 * After the button is clicked the task is started and the user get the points.
 */
public class WorkTask extends Task {
    /**
     * Database model of user.
     */
    private final User user;
    /**
     * Button at which the task is started.
     */
    private Button button;
    /**
     * Time to finish the task without modifiers.
     */
    private int basicTime;
    /**
     * Property which updates the time in GUI.
     */
    private SimpleIntegerProperty timeProperty = new SimpleIntegerProperty(this, "time to finish");
    /**
     * Position of task from 0 to 9.
     */
    private int position;

    public WorkTask(User user, Button button, int time, int position) {
        this.user = user;
        this.button = button;
        this.basicTime = time;
        this.position = position;
    }

    /**
     * Getter for timeProperty which is used for binding.
     *
     * @return IntegerProperty of time
     */
    public IntegerProperty timeProperty() {
        return timeProperty;
    }

    /**
     * Thread is waiting for time to decrease to 0.
     * After that user score is incremented.
     * If manager is present task starts again, else it finishes.
     *
     * @return null
     * @throws InterruptedException when sleep is interrupted
     */
    @Override
    protected Object call() {
        button.setDisable(true);
        do {
            int time = (int) (basicTime * (1 / user.getTimeModifier(position)));
            for (int i = 1; i <= time / 10; i++) {
                time = (int) (basicTime * (1 / user.getTimeModifier(position)));
                timeProperty.set((int) Math.ceil((time - i * 10) / 1000.0));
                updateProgress(i, time / 10);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("Sleep was interrupted.");
                }
                if (user.isBreakLoop()) {
                    if (position == 1) button.setDisable(false);
                    updateProgress(0, 100);
                    timeProperty.set(0);
                    return null;
                }
            }
            double score = Settings.money[position] * Math.pow(1.07, user.getLevel(position) - 1);
            score *= 1 + 0.02 * user.getAngelInvestors();
            synchronized (user) {
                user.addToScore(score);
            }
            updateProgress(0, 100);
        } while (user.isManagerPresent(position));
        button.setDisable(false);
        return null;
    }
}
