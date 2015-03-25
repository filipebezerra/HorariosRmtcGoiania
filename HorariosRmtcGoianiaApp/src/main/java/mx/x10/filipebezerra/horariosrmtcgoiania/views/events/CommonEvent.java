package mx.x10.filipebezerra.horariosrmtcgoiania.views.events;

/**
 * Commons events. This class is to be used in most cases, with string constants defined in
 * {@link mx.x10.filipebezerra.horariosrmtcgoiania.views.events.Event} class.
 *
 * @author Filipe Bezerra
 * @version 2.1, 25/03/2015
 * @since 2.0
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.views.events.Event
 */
public class CommonEvent implements Event<String> {

    protected String mMessage;

    public CommonEvent() {
    }

    public CommonEvent(String message) {
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
