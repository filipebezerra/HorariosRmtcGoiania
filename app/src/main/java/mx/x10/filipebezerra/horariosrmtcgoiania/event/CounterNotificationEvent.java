package mx.x10.filipebezerra.horariosrmtcgoiania.event;

/**
 * Event notification dispatched to badge views with a internal counter.
 *
 * @author Filipe Bezerra
 * @version 2.0, 27/02/2015
 * @since #
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.event.CounterNotificationMessage
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
