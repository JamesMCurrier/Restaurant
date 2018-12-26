/**
 * The Event class represents an Event. Events hold the id of it's intended recipient, as well as a line of text that
 * represent the Event that tells the recipient what to do.
 */
public class Event {

    // Whether or not this Event has already been received and handled.
    private boolean handled;

    // The type of event that this is
    private EventType eventType;

    // An the order that this event is about
    private Order order;

    // Employee that this event does not apply to
    private String exp;

    private String chefName;
    private String serverName;
    private String info = "";


    /**
     * Constructor for the Event class. Takes in the type of event
     * @param eventType The that tells the recipient what to do.
     */
    public Event(EventType eventType) {
        this.eventType = eventType;
    }

    public void addOrder(Order order){
        this.order = order;
    }

    public Order getOrder(){
        return this.order;
    }

    public void setExp(String exp){this.exp = exp;}

    public String getExp(){return exp;}

    /**
     * Returns the type of event this Event represents.
     * @return The type of event
     */
    public EventType getEventType(){
        return this.eventType;
    }

    /**
     * Returns whether or not this Event has been received by someone and has been handled.
     * @return True for yes, false for no.
     */
    public boolean isHandled() {
        return this.handled;
    }

    /**
     * Sets whether or not this Event has been received by someone and has been handled.
     * @param handled True for yes, false for no.
     */
    public void setHandled(boolean handled){
        this.handled = handled;
    }

    public void setChef(String chefName){
        this.chefName = chefName;
    }

    public String getChef(){
        return chefName;
    }

    public void setServer(String serverName){
        this.serverName = serverName;
    }

    public String getServer(){
        return serverName;
    }

    public void setInfo(String info){
        this.info = info;
    }

    public String getInfo(){
        return info;
    }
}
