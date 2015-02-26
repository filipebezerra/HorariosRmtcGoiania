package mx.x10.filipebezerra.horariosrmtcgoiania.event;

import android.os.Bundle;

import mx.x10.filipebezerra.horariosrmtcgoiania.model.NavDrawerItem;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 2.0, 25/02/2015
 * @since #
 */
public class DrawerItemSelectionMessage {
    
    private NavDrawerItem mNavDrawerItem;
    private Bundle mParams;

    public DrawerItemSelectionMessage() {
    }

    public DrawerItemSelectionMessage(final NavDrawerItem navDrawerItem, final Bundle params) {
        mNavDrawerItem = navDrawerItem;
        mParams = params;
    }

    public Bundle getParams() {
        return mParams;
    }

    public void setParams(final Bundle params) {
        mParams = params;
    }

    public NavDrawerItem getNavDrawerItem() {
        return mNavDrawerItem;
    }

    public void setNavDrawerItem(final NavDrawerItem navDrawerItem) {
        mNavDrawerItem = navDrawerItem;
    }
    
}
