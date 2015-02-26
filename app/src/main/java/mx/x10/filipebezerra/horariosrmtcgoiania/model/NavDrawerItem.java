package mx.x10.filipebezerra.horariosrmtcgoiania.model;

import java.io.Serializable;

/**
 * Navigation drawer item abstraction. This abstract is a model for 
 * {@link mx.x10.filipebezerra.horariosrmtcgoiania.model.NavMenuItem} and
 * {@link mx.x10.filipebezerra.horariosrmtcgoiania.model.NavMenuSection}.
 *  
 * @author Filipe Bezerra
 * @version 2.0, 02/26/2015
 * @since 2.0
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.model.NavMenuItem
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.model.NavMenuSection
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.adapter.NavDrawerAdapter
 */
public interface NavDrawerItem extends Serializable {

    int getId();
    String getLabel();
    int getType();
    boolean isEnabled();
    boolean updateActionBarTitle();

}
