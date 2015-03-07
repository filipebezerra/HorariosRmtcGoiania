package mx.x10.filipebezerra.horariosrmtcgoiania.ui.activity;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 2.0, 02/27/2015
 * @since #
 */
public class HomeActivity extends BaseActivity {

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();

    // TODO : handle notificationMessage bus event
    /**
    @Subscribe
    public void onNotificationEvent(NotificationMessage message) {
        MaterialSection section = getSectionByTitle(getString(
                R.string.navdrawer_menu_item_favorite_bus_stops));
        int actual = section.getNotifications();

        switch (message.getmNotificationType()) {
            case START:
                section.setNotifications(1);
                break;
            case INCREMENT:
                section.setNotifications(actual++);
                break;
            case DECREMENT:
                section.setNotifications(actual--);
                break;
            case RESET:
                section.setNotifications(0);
                break;
        }
    }
    **/
}