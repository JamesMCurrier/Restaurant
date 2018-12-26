/**
 * The EventReceiver interface. EventObservers are given Events, which they have to handle.
 */
public interface EventReceiver {

    /**
     * Handles the passed in Event.
     * @param event The Event that this EventReceiver has to handle.
     */
    void handleEvent(Event event);

}
