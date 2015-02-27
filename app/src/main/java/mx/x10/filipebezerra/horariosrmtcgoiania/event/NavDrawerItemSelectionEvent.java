package mx.x10.filipebezerra.horariosrmtcgoiania.event;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 2.0, 25/02/2015
 * @since #
 */
public class NavDrawerItemSelectionEvent implements Event<NavDrawerItemSelectionMessage> {

    public NavDrawerItemSelectionEvent() {
    }

    public NavDrawerItemSelectionEvent(NavDrawerItemSelectionMessage mNavDrawerItemSelectionMessage) {
        this.mNavDrawerItemSelectionMessage = mNavDrawerItemSelectionMessage;
    }

    private NavDrawerItemSelectionMessage mNavDrawerItemSelectionMessage;

    @Override
    public NavDrawerItemSelectionMessage getMessage() {
        return mNavDrawerItemSelectionMessage;
    }

    @Override
    public void setMessage(final NavDrawerItemSelectionMessage message) {
        mNavDrawerItemSelectionMessage = message;
    }
    
}
