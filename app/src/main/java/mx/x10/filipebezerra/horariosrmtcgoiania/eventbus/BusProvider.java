package mx.x10.filipebezerra.horariosrmtcgoiania.eventbus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Event bus provider (singleton class).
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 15/12/2015
 * @since 0.1.0
 * @see <a href="https://guides.codepath.com/android/Communicating-with-an-Event-Bus">Communicating with an Event Bus</a>
 */
public class BusProvider {
    private static final Bus BUS = new Bus(ThreadEnforcer.MAIN);

    private BusProvider() {
        // no instances
    }

    public static void post(final Object event) {
        BUS.post(event);
    }

    public static void register(Object obj) {
        BUS.register(obj);
    }

    public static void unregister(Object obj) {
        BUS.unregister(obj);
    }
}
