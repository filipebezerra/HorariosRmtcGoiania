package mx.x10.filipebezerra.horariosrmtcgoiania.event;

/**
 * Bean message class for communication between components using event bus.
 *
 * @author Filipe Bezerra
 * @version 2.0
 * @since 2.0_23/02/2015
 */
public class NavigationDrawerSelectionEvent {

    /**
     * The position accessed within the data set
     */
    private int mPosition;

    /**
     * The (@Link Gravity) side accessed.
     */
    private int mGravity;

    private boolean mUpdatable;

    private String mDescription;

    public NavigationDrawerSelectionEvent() {
        super();
    }

    public NavigationDrawerSelectionEvent(final int position, final int gravity) {
        this();
        mPosition = position;
        mGravity = gravity;
    }

    public NavigationDrawerSelectionEvent(final int position, final int gravity,
                                          boolean updatable, String description) {
        this(position, gravity);
        mUpdatable = updatable;
        mDescription = description;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public int getGravity() {
        return mGravity;
    }

    public void setGravity(int gravity) {
        mGravity = gravity;
    }


    public boolean isUpdatable() {
        return mUpdatable;
    }

    public void setUpdatable(boolean updatable) {
        mUpdatable = updatable;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

}
