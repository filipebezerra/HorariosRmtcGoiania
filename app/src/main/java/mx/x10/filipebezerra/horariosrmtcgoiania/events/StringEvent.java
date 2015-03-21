package mx.x10.filipebezerra.horariosrmtcgoiania.events;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 2.0, 02/25/2015
 * @since #
 */
public class StringEvent implements Event<String> {

    protected String mMessage;

    public StringEvent() {
    }

    public StringEvent(String message) {
        mMessage = message;
    }

    @Override
    public void setMessage(String message) {
        mMessage = message;
    }

    @Override
    public String getMessage() {
        return mMessage;
    }
    
}
