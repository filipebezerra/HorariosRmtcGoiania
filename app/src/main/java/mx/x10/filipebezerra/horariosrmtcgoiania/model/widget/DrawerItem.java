package mx.x10.filipebezerra.horariosrmtcgoiania.model.widget;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public class DrawerItem {

    private String title;

    private int iconRes;

    private String count = "0";

    private boolean isCounterVisible = false;

    public DrawerItem() {
        super();
    }

    public DrawerItem(final String title, final int iconRes) {
        this();
        this.title = title;
        this.iconRes = iconRes;
    }

    public DrawerItem(final String title, final int iconRes, final boolean isCounterVisible,
                      final String count) {
        this(title, iconRes);
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
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

}
