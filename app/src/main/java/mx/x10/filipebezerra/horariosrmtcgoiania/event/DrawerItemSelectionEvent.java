package mx.x10.filipebezerra.horariosrmtcgoiania.event;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 2.0, 25/02/2015
 * @since #
 */
public class DrawerItemSelectionEvent implements Event<DrawerItemSelectionMessage> {

    public DrawerItemSelectionEvent() {
    }

    public DrawerItemSelectionEvent(DrawerItemSelectionMessage mDrawerItemSelectionMessage) {
        this.mDrawerItemSelectionMessage = mDrawerItemSelectionMessage;
    }

    private DrawerItemSelectionMessage mDrawerItemSelectionMessage;

    @Override
    public DrawerItemSelectionMessage getMessage() {
        return mDrawerItemSelectionMessage;
    }

    @Override
    public void setMessage(final DrawerItemSelectionMessage message) {
        mDrawerItemSelectionMessage = message;
    }
    
}
