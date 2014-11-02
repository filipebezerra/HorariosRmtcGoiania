package mx.x10.filipebezerra.horariosrmtcgoiania.model.widget;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public class DrawerCategory {

    private String title;

    public DrawerCategory() {
        super();
    }

    public DrawerCategory(final String title) {
        this();
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
