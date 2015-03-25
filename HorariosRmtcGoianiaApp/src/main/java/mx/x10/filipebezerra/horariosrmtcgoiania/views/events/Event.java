package mx.x10.filipebezerra.horariosrmtcgoiania.views.events;

/**
 * Generic event used in communications between UI components.
 *
 * @param <T> The type of parsed message this event expects.
 * @author Filipe Bezerra
 * @version 2.0, 02/25/2015
 * @since #
 */
public interface Event<T> {
    public static final String INTERNET_CONNECTION_DISCONNECTED = "INTERNET_CONNECTION_DISCONNECTED";

    /**
     * @return
     */
    T getMessage();

    /**
     * @param message
     */
    void setMessage(T message);

}
