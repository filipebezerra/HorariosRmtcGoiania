package mx.x10.filipebezerra.horariosrmtcgoiania.ui.activity;

import com.squareup.otto.Subscribe;

import it.neokree.materialnavigationdrawer.elements.MaterialSection;
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
 * @version 2.0, 02/27/2015
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
        MaterialSection section = getSectionByTitle(getString(
                R.string.navdrawer_section_favorite_bus_stops));
        int notifications = section.getNotifications();
        NotificationMessage.NotificationType notificationType = event.getMessage()
                .getNotificationType();

        LOGD(LOG_TAG, String.format(getString(R.string.log_event_debug),
                "onNotificationEvent", notificationType, "update the notification"));

        switch (notificationType) {
            case START:
                section.setNotifications(1);
                break;
            case INCREMENT:
                section.setNotifications(++notifications);
                break;
            case DECREMENT:
                section.setNotifications(--notifications);
                break;
            case RESET:
                section.setNotifications(0);
                break;
        }
    }
}