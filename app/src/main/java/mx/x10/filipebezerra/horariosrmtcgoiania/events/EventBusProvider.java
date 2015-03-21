package mx.x10.filipebezerra.horariosrmtcgoiania.events;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * @author Filipe Bezerra
 * @version #, 25/02/2015
 * @since #
 */
public class EventBusProvider {

    private static final String LOG_TAG = EventBusProvider.class.getSimpleName();
    
    private static EventBusProvider mInstance = new EventBusProvider();

    private Bus eventBus;

    public static EventBusProvider getInstance() {
        return mInstance;
    }

    private EventBusProvider() {
    }

    /**
     * Lazy loading event bus.
     *
     * @return Event bus service.
     */
    public Bus getEventBus() {
        if (eventBus == null) {
            eventBus = new Bus(ThreadEnforcer.MAIN, LOG_TAG);
        }
        return eventBus;
    }

}
