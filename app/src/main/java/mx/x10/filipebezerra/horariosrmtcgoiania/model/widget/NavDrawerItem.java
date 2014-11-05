package mx.x10.filipebezerra.horariosrmtcgoiania.model.widget;

/**
 * @author Michenux (http://www.michenux.net/android-navigation-drawer-748.html)
 * @since 2.0
 */
public interface NavDrawerItem {

    int getId();
    String getLabel();
    int getType();
    boolean isEnabled();
    boolean updateActionBarTitle();

}
