package mx.x10.filipebezerra.horariosrmtcgoiania.ui.activities;

import com.squareup.otto.Subscribe;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.events.FavoriteItemSelectionEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.events.NotificationEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.events.NotificationMessage;
import timber.log.Timber;

/**
 * Main activity which hosts the Navigation Drawer.
 *
 * @author Filipe Bezerra
 * @version 2.1, 20/03/2015
 * @since 2.0
 */
public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    @Subscribe
    public void onFavoriteItemSelectionEvent(FavoriteItemSelectionEvent event) {
        int stopCode = event.getMessage().getStopCode();

        Timber.d(String.format(getString(R.string.log_event_debug),
                "onFavoriteItemSelectionEvent", "bus stop: "+stopCode, " load stop bus"));

        searchStopCode(String.valueOf(stopCode));
    }

    @Subscribe
    public void onNotificationEvent(NotificationEvent event) {
        int notifications = favoriteBusStopSection.getNotifications();
        NotificationMessage.NotificationType notificationType = event.getMessage()
                .getNotificationType();

        Timber.d(String.format(getString(R.string.log_event_debug),
                "onNotificationEvent", notificationType, "update the notification"));

        switch (notificationType) {
            case START:
                favoriteBusStopSection.setNotifications(1);
                break;
            case INCREMENT:
                favoriteBusStopSection.setNotifications(++notifications);
                break;
            case DECREMENT:
                favoriteBusStopSection.setNotifications(--notifications);
                break;
            case RESET:
                favoriteBusStopSection.setNotifications(0);
                break;
        }
    }
}