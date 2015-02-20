package mx.x10.filipebezerra.horariosrmtcgoiania.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.NavDrawerItem;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.util.fragment.NavDrawerActivityConfiguration;

/**
 * @author Filipe Bezerra
 *          Michenux (http://www.michenux.net/android-navigation-drawer-748.html)
 * @since 2.0
 */
public abstract class AbstractNavDrawerActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private ListView mLeftDrawerList;
    private ListView mRightDrawerList;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private NavDrawerActivityConfiguration navConf ;

    protected abstract NavDrawerActivityConfiguration getNavDrawerConfiguration();

    protected abstract void onNavItemSelected( int id );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navConf = getNavDrawerConfiguration();

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(navConf.getDrawerLayoutId());
        mLeftDrawerList = (ListView) findViewById(navConf.getLeftDrawerId());
        mLeftDrawerList.setAdapter(navConf.getLeftNavAdapter());
        mLeftDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mRightDrawerList = (ListView) findViewById(navConf.getRightDrawerId());
        mRightDrawerList.setAdapter(navConf.getRightNavAdapter());

        this.initDrawerShadow();
        this.initDrawerIcon();

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                navConf.getDrawerOpenDesc(),
                navConf.getDrawerCloseDesc()
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setSubtitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setSubtitle(null);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    protected void initDrawerIcon() {
        setActionBarIcon(R.drawable.ic_menu_white_24dp);
    }

    protected void initDrawerShadow() {
        int drawerShadow = navConf.getDrawerShadow();
        if (drawerShadow != 0) {
            mDrawerLayout.setDrawerShadow(drawerShadow, GravityCompat.START);
            mDrawerLayout.setDrawerShadow(drawerShadow, GravityCompat.END);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (navConf.getActionMenuItemsToHideWhenDrawerOpen() != null) {
            boolean drawerOpen = mDrawerLayout.isDrawerOpen(mLeftDrawerList);
            for(int iItem : navConf.getActionMenuItemsToHideWhenDrawerOpen()) {
                menu.findItem(iItem).setVisible(!drawerOpen);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (this.mDrawerLayout.isDrawerOpen(this.mLeftDrawerList)) {
                this.mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
            else {
                if (this.mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    this.mDrawerLayout.closeDrawer(Gravity.RIGHT);
                }

                this.mDrawerLayout.openDrawer(Gravity.LEFT);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if ( this.mDrawerLayout.isDrawerOpen(this.mLeftDrawerList)) {
            mDrawerLayout.closeDrawer(mLeftDrawerList);
            return;
        }
        super.onBackPressed();
    }

    protected DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    protected ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    public void selectItem(int position) {
        NavDrawerItem selectedItem = navConf.getLeftNavItems()[position];

        this.onNavItemSelected(selectedItem.getId());
        mLeftDrawerList.setItemChecked(position, true);

        if ( selectedItem.updateActionBarSubtitle()) {
            setTitle(selectedItem.getLabel());
        }

        if ( this.mDrawerLayout.isDrawerOpen(this.mLeftDrawerList)) {
            mDrawerLayout.closeDrawer(mLeftDrawerList);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setSubtitle(mTitle);
    }

    public void openLeftDrawer(){
        if (mDrawerLayout != null) {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    public void openRightDrawer(){
        if (mDrawerLayout != null) {
            mDrawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

}