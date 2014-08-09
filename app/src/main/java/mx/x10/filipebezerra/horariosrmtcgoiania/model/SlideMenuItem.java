package mx.x10.filipebezerra.horariosrmtcgoiania.model;

/**
 * Classe modelo dos itens do slide menu.
 *
 * @author Filipe Bezerra
 * @since 1.3
 */
public class SlideMenuItem {
    public String title;
    public String description;
    public int iconRes;

    public SlideMenuItem(String title, String description, int iconRes) {
        this.title = title;
        this.description = description;
        this.iconRes = iconRes;
    }
}
