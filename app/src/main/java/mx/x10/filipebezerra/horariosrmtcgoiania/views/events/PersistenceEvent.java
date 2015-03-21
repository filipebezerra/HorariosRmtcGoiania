package mx.x10.filipebezerra.horariosrmtcgoiania.views.events;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 2.0, 02/25/2015
 * @since #
 */
public class PersistenceEvent implements Event<PersistenceMessage> {

    protected PersistenceMessage mMessage;

    public PersistenceEvent() {
        
    }

    public PersistenceEvent(PersistenceMessage message) {
        mMessage = message;
    }

    @Override
    public PersistenceMessage getMessage() {
        return mMessage;
    }

    @Override
    public void setMessage(PersistenceMessage message) {
        mMessage = message;
    }
    
}
