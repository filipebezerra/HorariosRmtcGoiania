package mx.x10.filipebezerra.horariosrmtcgoiania.event;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version #, 27/02/2015
 * @since #
 */
public class CounterNotificationEvent implements Event<CounterNotificationMessage> {
    
    private CounterNotificationMessage mMessage;

    public CounterNotificationEvent() {
    }

    public CounterNotificationEvent(CounterNotificationMessage message) {
        this.mMessage = message;
    }

    @Override
    public CounterNotificationMessage getMessage() {
        return mMessage;
    }

    @Override
    public void setMessage(CounterNotificationMessage message) {
        mMessage = message;
    }
    
}
