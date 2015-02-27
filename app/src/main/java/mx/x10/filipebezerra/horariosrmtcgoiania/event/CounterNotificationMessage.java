package mx.x10.filipebezerra.horariosrmtcgoiania.event;

/**
 * Event message for {@link mx.x10.filipebezerra.horariosrmtcgoiania.event.CounterNotificationEvent}.
 *
 * @author Filipe Bezerra
 * @version 2.0, 27/02/2015
 * @since #
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.event.CounterNotificationEvent
 */
public class CounterNotificationMessage {
    
    public enum NotificationType {
        START, INCREMENT, DECREMENT, RESET
    }
    
    private int mViewId;
    private NotificationType mNotificationType;

    public CounterNotificationMessage() {
    }

    public CounterNotificationMessage(int mViewId, NotificationType mNotificationType) {
        this.mViewId = mViewId;
        this.mNotificationType = mNotificationType;
    }

    public int getmViewId() {
        return mViewId;
    }

    public void setmViewId(int mViewId) {
        this.mViewId = mViewId;
    }

    public NotificationType getmNotificationType() {
        return mNotificationType;
    }

    public void setmNotificationType(NotificationType mNotificationType) {
        this.mNotificationType = mNotificationType;
    }
    
}
