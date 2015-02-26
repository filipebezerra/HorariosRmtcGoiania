package mx.x10.filipebezerra.horariosrmtcgoiania.model;

/**
 * Stylized navigation drawer section. Represents the label for a set of 
 * {@link mx.x10.filipebezerra.horariosrmtcgoiania.model.NavMenuItem}.
 *
 * @author Filipe Bezerra 
 * @version 2.0, 02/25/2015
 * @since 2.0
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.model.NavDrawerItem
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.model.NavMenuItem
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.adapter.NavDrawerAdapter 
 */
public class NavMenuSection implements NavDrawerItem {

    public static final int SECTION_TYPE = 0;
    private int id;
    private String label;

    private NavMenuSection() {
    }

    public static NavMenuSection create(int id, String label) {
        NavMenuSection section = new NavMenuSection();
        section.setLabel(label);
        return section;
    }

    @Override
    public int getType() {
        return SECTION_TYPE;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean updateActionBarTitle() {
        return false;
    }

}