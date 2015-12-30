package mx.x10.filipebezerra.horariosrmtcgoiania.eventbus;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Event bus provider (singleton class).
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 15/12/2015
 * @since 0.1.0
 * @see <a href="https://guides.codepath.com/android/Communicating-with-an-Event-Bus">Communicating with an Event Bus</a>
 */
public class BusProvider {
    private static final Bus BUS = new Bus();

    private BusProvider() {
        // no instances
    }

    private static final Handler mainThread = new Handler(Looper.getMainLooper());

    public static void postOnMain(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            BUS.post(event);
        } else {
            mainThread.post(() -> BUS.post(event));
        }
    }

    public static void register(Object obj) {
        BUS.register(obj);
    }

    public static void unregister(Object obj) {
        BUS.unregister(obj);
    }
}
