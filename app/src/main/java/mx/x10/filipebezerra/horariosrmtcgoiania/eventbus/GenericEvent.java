package mx.x10.filipebezerra.horariosrmtcgoiania.eventbus;

/**
 * Generic event bus.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 15/12/2015
 * @since 0.1.0
 */
public class GenericEvent<M> implements Event<M> {
    private M mMessage;

    public GenericEvent(M message) {
        this.mMessage = message;
    }

    @Override
    public M message() {
        return mMessage;
    }
}
