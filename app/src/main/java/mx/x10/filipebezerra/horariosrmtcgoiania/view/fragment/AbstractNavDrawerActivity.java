package mx.x10.filipebezerra.horariosrmtcgoiania.view.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.NavDrawerItem;
import mx.x10.filipebezerra.horariosrmtcgoiania.view.activity.BaseActivity;
import mx.x10.filipebezerra.horariosrmtcgoiania.view.util.fragment.NavDrawerActivityConfiguration;

/**
 * @author Filipe Bezerra
 *          Michenux (http://www.michenux.net/android-navigation-drawer-748.html)
 * @since 2.0
 */
public abstract class AbstractNavDrawerActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private ListView mDrawerList;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private NavDrawerActivityConfiguration navConf ;

    protected abstract NavDrawerActivityConfiguration getNavDrawerConfiguration();

    protected abstract void onNavItemSelected( int id );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navConf = getNavDrawerConfiguration();

        setContentView(navConf.getMainLayout());

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(navConf.getDrawerLayoutId());
        mDrawerList = (ListView) findViewById(navConf.getLeftDrawerId());
        mDrawerList.setAdapter(navConf.getBaseAdapter());
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        //this.initDrawerShadow();

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                navConf.getDrawerOpenDesc(),
                navConf.getDrawerCloseDesc()
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        this.getDrawerIcon();
    }

    protected void initDrawerShadow() {
        mDrawerLayout.setDrawerShadow(navConf.getDrawerShadow(), GravityCompat.START);
    }

    protected int getDrawerIcon() {
        return R.drawable.ic_menu_white_24dp;
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
            boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
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
            if (this.mDrawerLayout.isDrawerOpen(this.mDrawerList)) {
                this.mDrawerLayout.closeDrawer(this.mDrawerList);
            }
            else {
                this.mDrawerLayout.openDrawer(this.mDrawerList);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if ( this.mDrawerLayout.isDrawerOpen(this.mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
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
        NavDrawerItem selectedItem = navConf.getNavItems()[position];

        this.onNavItemSelected(selectedItem.getId());
        mDrawerList.setItemChecked(position, true);

        if ( selectedItem.updateActionBarTitle()) {
            setTitle(selectedItem.getLabel());
        }

        if ( this.mDrawerLayout.isDrawerOpen(this.mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
}