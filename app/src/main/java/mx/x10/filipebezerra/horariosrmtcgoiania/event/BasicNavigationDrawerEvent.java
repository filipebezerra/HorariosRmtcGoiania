package mx.x10.filipebezerra.horariosrmtcgoiania.event;

/**
 *
 *
 * @author Filipe Bezerra
 * @version 2.0
 * @since 2.0_24/02/2015
 */
public class BasicNavigationDrawerEvent {

    private int mId;

    public BasicNavigationDrawerEvent(int id) {
        this.mId = id;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

}
