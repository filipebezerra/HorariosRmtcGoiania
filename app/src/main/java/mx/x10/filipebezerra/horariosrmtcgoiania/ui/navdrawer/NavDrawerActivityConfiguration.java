package mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public class NavDrawerActivityConfiguration {
    private int mDrawerShadow;
    private int mDrawerLayoutId;
    private int mDrawerOpenDesc;
    private int mDrawerCloseDesc;

    public int getDrawerShadow() {
        return mDrawerShadow;
    }

    public void setDrawerShadow(int drawerShadow) {
        this.mDrawerShadow = drawerShadow;
    }

    public int getDrawerLayoutId() {
        return mDrawerLayoutId;
    }

    public void setDrawerLayoutId(int drawerLayoutId) {
        this.mDrawerLayoutId = drawerLayoutId;
    }

    public int getDrawerOpenDesc() {
        return mDrawerOpenDesc;
    }

    public void setDrawerOpenDesc(int drawerOpenDesc) {
        this.mDrawerOpenDesc = drawerOpenDesc;
    }

    public int getDrawerCloseDesc() {
        return mDrawerCloseDesc;
    }

    public void setDrawerCloseDesc(int drawerCloseDesc) {
        this.mDrawerCloseDesc = drawerCloseDesc;
    }

}