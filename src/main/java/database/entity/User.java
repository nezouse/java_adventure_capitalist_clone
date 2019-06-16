package database.entity;

import io.ebean.Model;
import io.ebean.annotation.DbJson;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import io.ebeaninternal.json.ModifyAwareList;
import javafx.beans.property.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

/**
 * Model for every user in the game.<br>
 * It inherits from ebean.Model which is base for Ebean ORM.
 *
 * @see "https://ebean.io/"
 */
@Entity
@Table(name = "UserEntity")
public class User extends Model {
    /**
     * Field to access queries to database.
     *
     * @see UserFinder
     */
    public static final UserFinder find = new UserFinder();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String login;
    private int angelInvestors;
    private double lifetimeScore;
    @Transient private SimpleDoubleProperty score = new SimpleDoubleProperty(this, "score");
    @Transient private List<SimpleIntegerProperty> levels = new ArrayList<>();
    @Transient private List<Integer> timeModifier = new ArrayList<>();
    @Transient private List<Boolean> managers = new ArrayList<>();
    @WhenCreated private Instant whenCreated;
    @WhenModified private Instant whenModified;
    @DbJson private Map<String, Object> content = new HashMap<>();
    @Transient private SimpleBooleanProperty breakLoop = new SimpleBooleanProperty(false);

    /**
     * Constructor which we use to create a new user.
     *
     * @param username username of the user we create
     */
    public User(String username) {
        this.login = username;
        score.set(0);
        lifetimeScore = 0;
        angelInvestors = 0;
        for (int i = 0; i < 10; i++) {
            levels.add(new SimpleIntegerProperty(this, "level"));
            managers.add(false);
            timeModifier.add(1);
        }
        setLevel(0, 1);
        content.put("score", getScore());
        content.put("levels", getLevelsList());
        content.put("managers", managers);
        content.put("timeModifiers", timeModifier);
    }

    public double getAngelInvestors() {
        return angelInvestors;
    }

    public double getLifetimeScore() {
        return lifetimeScore;
    }

    /**
     * As the library which is used for database management use only basic java types the more complex one are contained in map.
     * After loading user from database values from map are loaded to the arrays.
     */
    public void initialize() {
        score = new SimpleDoubleProperty();
        score.set(((BigDecimal) content.get("score")).doubleValue());
        managers = new ArrayList<>();
        Arrays.stream(((ModifyAwareList) content.get("managers")).toArray()).forEach(o -> managers.add((boolean) o));
        timeModifier = new ArrayList<>();
        Arrays.stream(((ModifyAwareList) content.get("timeModifiers")).toArray()).forEach(o -> timeModifier.add((int) (long) o));
        levels = new ArrayList<>();
        Arrays.stream(((ModifyAwareList) content.get("levels")).toArray()).forEach(o -> levels.add(new SimpleIntegerProperty((int) (long) o)));
        breakLoop = new SimpleBooleanProperty(false);
    }


    public double getScore() {
        return score.get();
    }

    /**
     * Adds score to the user's score and user's lifetime score.
     *
     * @param score value which is added
     */
    public void addToScore(double score) {
        this.score.set(this.score.get() + score);
        lifetimeScore += score;
        content.put("score", getScore());
    }

    /**
     * Substract from user's score.
     *
     * @param score value to substract
     */
    public void substractFromScore(double score) {
        this.score.set(this.score.get() - score);
        content.put("score", getScore());
    }

    public DoubleProperty scoreProperty() {
        return score;
    }

    public List<Integer> getLevelsList() {
        ArrayList<Integer> array = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            array.add(levels.get(i).get());
        }
        return array;
    }

    public int getLevel(int i) {
        return levels.get(i).get();
    }

    public void setLevel(int i, int level) {
        levels.get(i).set(level);
        content.put("levels", getLevelsList());
    }

    public IntegerProperty levelProperty(int i) {
        return levels.get(i);
    }

    public double getTimeModifier(int index) {
        return timeModifier.get(index);
    }

    public boolean isManagerPresent(int i) {
        return managers.get(i);
    }

    public void setManager(int index, boolean b) {
        managers.set(index, b);
        content.put("managers", managers);
    }

    public void reduceTimer(int index) {
        timeModifier.set(index, timeModifier.get(index) * 2);
        content.put("timeModifiers", timeModifier);
    }

    public String getLogin() {
        return login;
    }

    public void claimAngels(int angels) {
        angelInvestors += angels;
        score.set(0);
        lifetimeScore = 0;
        angelInvestors = 0;
        for (int i = 0; i < 10; i++) {
            levels.get(i).set(0);
            managers.set(i, false);
            timeModifier.set(i, 1);
        }
        setLevel(0, 1);
        content.put("score", getScore());
        content.put("levels", getLevelsList());
        content.put("managers", managers);
        content.put("timeModifiers", timeModifier);
        breakLoop.set(true);
        try {
            Thread.sleep(50);
        } catch (Exception e) {
        }
        breakLoop.set(false);
        this.save();
    }

    public BooleanProperty breakLoopProperty() {
        return breakLoop;
    }

    public boolean isBreakLoop() {
        return breakLoop.get();
    }
}
