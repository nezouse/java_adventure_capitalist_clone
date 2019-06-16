package helper;

/**
 * Settings holds all const values which are accessed to control the game.
 */
public class Settings {
    /**
     * How much money business gives at 1st level.
     */
    public static final long[] money = {1, 60, 540, 4320, 51840, 622080, 7464960, 89579520, 89579520, 29668737024L};
    /**
     * How much time the task takes to finish. (1000 = 1s)
     */
    public static final int[] time = {600, 3000, 6000, 12000, 24000, 96000, 384000, 1536000, 6144000, 36864000};
    public static final String[] managerNames = {"Cabe Johnson", "Perry Black", "W.W. Heisenbird", "Mama Sean",
            "Jim Thorton", "Forest Trump", "Dawn Cheri", "Stefani Speilburger ", "The Dark Lord", "Derrick Plainview"};
    public static final String[] managerDescriptions = {"Lemonade Stand", "Newspaper Delivery", "Car Wash", "Pizza Delivery",
            "Donut Shop", "Shrimp Boat", "Hockey Team", "Movie Studio", "Bank", "Oil Company"};
    public static final long[] managerCosts = {1000, 15000, 100000, 500000, 1200000, 10000000, 111111111, 555555555,
            10000000000L, 100000000000L};
    /**
     * Levels at which time modifier is decreased.
     */
    public static final int[] timeDecrease = {25, 50, 100, 200, 300, 400};
    /**
     * Cost to buy business at 1st level.
     */
    public static final double[] costs = {3.738, 60, 720, 8640, 103680, 1244160, 14929920, 179159040, 2149908480L, 25798901760L};
}
