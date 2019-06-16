package database.entity;

import io.ebean.Finder;

/**
 * Class which implements all queries to database.
 * It extends Finder which lets only query byId.
 */
public class UserFinder extends Finder<Integer, User> {
    public UserFinder() {
        super(User.class);
    }

    /**
     * Get user from database by name.
     * @param username name of the user we want to get from database
     * @return User instance or null if user is not present
     */
    public User byName(String username) {
        return query().where().eq("login", username).findOne();
    }
}
