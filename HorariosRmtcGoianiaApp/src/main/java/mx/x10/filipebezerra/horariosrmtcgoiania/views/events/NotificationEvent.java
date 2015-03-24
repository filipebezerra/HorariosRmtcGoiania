package mx.x10.filipebezerra.horariosrmtcgoiania.views.events;

/**
 * Event notification dispatched to badge views with a internal counter.
 *
 * @author Filipe Bezerra
 * @version 2.0, 06/03/2015
 * @since #
 * @see NotificationMessage
 */
public class NotificationEvent implements Event<NotificationMessage> {
    
    private NotificationMessage mMessage;

    public NotificationEvent(NotificationMessage message) {
        this.mMessage = message;
    }

    @Override
    public NotificationMessage getMessage() {
        return mMessage;
    }

    @Override
    public void setMessage(NotificationMessage message) {
        mMessage = message;
    }
    
}
