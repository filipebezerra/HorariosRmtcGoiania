package mx.x10.filipebezerra.horariosrmtcgoiania.ui.util.fragment;

import android.widget.BaseAdapter;

import java.util.List;

import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.NavDrawerItem;

/**
 * @author Michenux (http://www.michenux.net/android-navigation-drawer-748.html)
 * @since 2.0
 */
public class NavDrawerActivityConfiguration {
    private int drawerShadow;
    private int drawerLayoutId;
    private int leftDrawerId;
    private int rightDrawerId;
    private int[] actionMenuItemsToHideWhenDrawerOpen;
    private NavDrawerItem[] leftNavItems;
    private List<NavDrawerItem> rightNavItems;
    private int drawerOpenDesc;
    private int drawerCloseDesc;
    private BaseAdapter mLeftNavAdapter;
    private BaseAdapter mRightNavAdapter;

    public int getDrawerShadow() {
        return drawerShadow;
    }

    public void setDrawerShadow(int drawerShadow) {
        this.drawerShadow = drawerShadow;
    }

    public int getDrawerLayoutId() {
        return drawerLayoutId;
    }

    public void setDrawerLayoutId(int drawerLayoutId) {
        this.drawerLayoutId = drawerLayoutId;
    }

    public int getLeftDrawerId() {
        return leftDrawerId;
    }

    public void setLeftDrawerId(int leftDrawerId) {
        this.leftDrawerId = leftDrawerId;
    }

    public int getRightDrawerId() {
        return rightDrawerId;
    }

    public void setRightDrawerId(final int rightDrawerId) {
        this.rightDrawerId = rightDrawerId;
    }

    public int[] getActionMenuItemsToHideWhenDrawerOpen() {
        return actionMenuItemsToHideWhenDrawerOpen;
    }

    public void setActionMenuItemsToHideWhenDrawerOpen(
            int[] actionMenuItemsToHideWhenDrawerOpen) {
        this.actionMenuItemsToHideWhenDrawerOpen = actionMenuItemsToHideWhenDrawerOpen;
    }

    public NavDrawerItem[] getLeftNavItems() {
        return leftNavItems;
    }

    public void setLeftNavItems(NavDrawerItem[] leftNavItems) {
        this.leftNavItems = leftNavItems;
    }

    public List<NavDrawerItem> getRightNavItems() {
        return rightNavItems;
    }

    public void setRightNavItems(final List<NavDrawerItem> rightNavItems) {
        this.rightNavItems = rightNavItems;
    }

    public int getDrawerOpenDesc() {
        return drawerOpenDesc;
    }

    public void setDrawerOpenDesc(int drawerOpenDesc) {
        this.drawerOpenDesc = drawerOpenDesc;
    }

    public int getDrawerCloseDesc() {
        return drawerCloseDesc;
    }

    public void setDrawerCloseDesc(int drawerCloseDesc) {
        this.drawerCloseDesc = drawerCloseDesc;
    }

    public BaseAdapter getLeftNavAdapter() {
        return mLeftNavAdapter;
    }

    public void setLeftNavAdapter(BaseAdapter leftNavAdapter) {
        this.mLeftNavAdapter = leftNavAdapter;
    }

    public BaseAdapter getRightNavAdapter() {
        return mRightNavAdapter;
    }

    public void setRightNavAdapter(final BaseAdapter rightNavAdapter) {
        mRightNavAdapter = rightNavAdapter;
    }

}