package task;

import database.entity.User;

/**
 * Thread which is used to save the game every 10 minutes.
 */
public class AutosaveTask extends Thread {
    private final User user;

    /**
     * Basic constructor to create thread.
     *
     * @param user game user
     */
    public AutosaveTask(User user) {
        this.user = user;
    }

    /**
     * Thread task which save the game state synchronised on user model.
     */
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(60000);
            } catch (Exception e) {
                System.out.println("Sleep was interrupted: " + e);
            }
            synchronized (user) {
                user.save();
            }
        }
    }
}
