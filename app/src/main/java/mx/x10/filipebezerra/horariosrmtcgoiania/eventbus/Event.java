package mx.x10.filipebezerra.horariosrmtcgoiania.eventbus;

/**
 * Event bus base interface.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 15/12/2015
 * @since 0.1.0
 */
public interface Event<M> {
    M message();
}
