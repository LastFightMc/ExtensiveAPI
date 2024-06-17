package fr.skoupi.extensiveapi.core.cooldown;

/*  Cooldown
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

@SuppressWarnings("unused")
public interface Cooldown<T> {

    /**
     * Adds a timer to the cooldown manager
     *
     * @param object The object to which the cooldown will be attached
     * @param cooldownIdentifier This is the identifier for the cooldown.
     * @param timeInMs The time in ms that the cooldown will last.
     */
    void addMsTimer(T object, String cooldownIdentifier, long timeInMs);


    /**
     * Adds a timer to the cooldown manager
     *
     * @param object The object to which the cooldown will be attached
     * @param cooldownIdentifier This is the identifier for the cooldown.
     * @param time The time in seconds that the cooldown will last.
     */
    void addTimer(T object, String cooldownIdentifier, long time);

    /**
     * Returns true if the object is in the cooldown timer, false otherwise.
     *
     * @param object The object that you want to check if it's in a timer.
     * @param cooldownIdentifier The identifier for the cooldown.
     * @return A boolean value.
     */
    boolean isInTimer(T object, String cooldownIdentifier);

    /**
     * "Returns the time in milliseconds when the cooldown will expire."
     * The first parameter is the object that is being checked. The second parameter is the cooldown identifier
     *
     * @param object The object that is being checked.
     * @param cooldownIdentifier The identifier for the cooldown.
     * @return The time in milliseconds that the cooldown will expire.
     */
    long getTime(T object, String cooldownIdentifier);

}
