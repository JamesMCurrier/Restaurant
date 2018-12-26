import java.util.ArrayList;

/**
 * EventCreator interface. Whenever it creates an Event, it notifies all EventReceivers watching it and sends them
 * the Event.
 */
public interface EventCreator {

    /**
     * Add the specified EventReceiver object to listen to this EventCreator.
     * @param eventReceiver The EventReceiver to add.
     */
    void addListener(EventReceiver eventReceiver);

    /**
     * Remove the specified EventReceiver object from listening to this EventCreator.
     * @param eventReceiver The EventReceiver to remove.
     */
    void removeListener(EventReceiver eventReceiver);

    /**
     * Notify and send to all EventObservers listening in of the Event.
     * @param event The Event to send to the EventObservers.
     */
    void sendEvent(Event event);
}
