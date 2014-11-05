package mx.x10.filipebezerra.horariosrmtcgoiania.model.widget;

import android.content.Context;

/**
 * @author Filipe Bezerra
 *          Michenux (http://www.michenux.net/android-navigation-drawer-748.html)
 * @since 2.0
 */
public class NavMenuItem implements NavDrawerItem {

    public static final int ITEM_TYPE = 1 ;

    private int id;
    private String label;
    private int icon;
    private String count = "0";
    private boolean isCounterVisible = false;
    private boolean updateActionBarTitle;

    private NavMenuItem() {
    }

    public static NavMenuItem create(int id, String label, String icon, boolean updateActionBarTitle,
                                     Context context) {
        NavMenuItem item = new NavMenuItem();
        item.setId(id);
        item.setLabel(label);
        item.setIcon(context.getResources().getIdentifier(icon, "drawable", context.getPackageName()));
        item.setUpdateActionBarTitle(updateActionBarTitle);
        return item;
    }

    public static NavMenuItem create(int id, String label, String icon, String count,
                                     boolean isCounterVisible, boolean updateActionBarTitle,
                                     Context context) {
        NavMenuItem item = new NavMenuItem();
        item.setId(id);
        item.setLabel(label);
        item.setIcon(context.getResources().getIdentifier(icon, "drawable", context.getPackageName()));
        item.setCount(count);
        item.setCounterVisible(isCounterVisible);
        item.setUpdateActionBarTitle(updateActionBarTitle);
        return item;
    }

    @Override
    public int getType() {
        return ITEM_TYPE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public boolean isCounterVisible() {
        return isCounterVisible;
    }

    public void setCounterVisible(boolean isCounterVisible) {
        this.isCounterVisible = isCounterVisible;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean updateActionBarTitle() {
        return this.updateActionBarTitle;
    }

    public void setUpdateActionBarTitle(boolean updateActionBarTitle) {
        this.updateActionBarTitle = updateActionBarTitle;
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public boolean equals(Object other) {
        if (other != null) {
            if (other instanceof NavMenuItem) {
                NavMenuItem otherItem = (NavMenuItem) other;

                return label != null && otherItem.getLabel() != null &&
                        label.equals(otherItem.getLabel());
            }
        }

        return false;
    }
}
