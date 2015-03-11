package mx.x10.filipebezerra.horariosrmtcgoiania.ui.activity;

import com.squareup.otto.Subscribe;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.FavoriteItemSelectionEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.NotificationEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.NotificationMessage;

import static mx.x10.filipebezerra.horariosrmtcgoiania.util.LogUtils.LOGD;
import static mx.x10.filipebezerra.horariosrmtcgoiania.util.LogUtils.makeLogTag;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 2.0, 10/03/2015
 * @since #
 */
public class HomeActivity extends BaseActivity {

    private static final String LOG_TAG = makeLogTag(HomeActivity.class);

    @Subscribe
    public void onFavoriteItemSelectionEvent(FavoriteItemSelectionEvent event) {
        int stopCode = event.getMessage().getStopCode();

        LOGD(LOG_TAG, String.format(getString(R.string.log_event_debug),
                "onFavoriteItemSelectionEvent", "bus stop: "+stopCode, " load stop bus"));

        searchStopCode(String.valueOf(stopCode));
    }

    @Subscribe
    public void onNotificationEvent(NotificationEvent event) {
        int notifications = favoriteBusStopSection.getNotifications();
        NotificationMessage.NotificationType notificationType = event.getMessage()
                .getNotificationType();

        LOGD(LOG_TAG, String.format(getString(R.string.log_event_debug),
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