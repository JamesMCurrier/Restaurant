/**
 * A listener interface that listens in on Dialog close events.
 */
public interface DialogCloseListener {

    /**
     * Method that gets called whenever a Dialog has been closed. Handles any changes caused by the Dialogs.
     * @param dialog The Dialog that just closed.
     */
    void dialogClosed(Dialog dialog);
}
