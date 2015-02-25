package mx.x10.filipebezerra.horariosrmtcgoiania.model;

import java.io.Serializable;

/**
 * @author Michenux (http://www.michenux.net/android-navigation-drawer-748.html)
 * @since 2.0
 */
public interface NavDrawerItem extends Serializable {

    int getId();
    String getLabel();
    int getType();
    boolean isEnabled();
    boolean updateActionBarSubtitle();

}
