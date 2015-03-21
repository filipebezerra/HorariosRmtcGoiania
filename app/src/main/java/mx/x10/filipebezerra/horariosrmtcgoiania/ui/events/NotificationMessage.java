package mx.x10.filipebezerra.horariosrmtcgoiania.ui.events;

/**
 * Event message for {@link NotificationEvent}.
 *
 * @author Filipe Bezerra
 * @version 2.0, 06/03/2015
 * @since #
 * @see NotificationEvent
 */
public class NotificationMessage {
    
    public enum NotificationType {
        START, INCREMENT, DECREMENT, RESET
    }

    private NotificationType mNotificationType;

    public NotificationMessage(NotificationType mNotificationType) {
        this.mNotificationType = mNotificationType;
    }

    public NotificationType getNotificationType() {
        return mNotificationType;
    }

    public void setNotificationType(NotificationType mNotificationType) {
        this.mNotificationType = mNotificationType;
    }
    
}
