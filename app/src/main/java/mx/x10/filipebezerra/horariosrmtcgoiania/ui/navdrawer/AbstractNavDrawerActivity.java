package mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.DrawerItemSelectionEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.activity.BaseActivity;

/**
 * Abstraction of a Navigation Drawer containing configuration and behavior to Drawers 
 * {@link mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer.LeftDrawerFragment} and
 * {@link mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer.RightDrawerFragment}.
 *  
 * @author Filipe Bezerra
 * @version 2.0, 02/26/2015
 * @since 2.0
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer.LeftDrawerFragment
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer.RightDrawerFragment
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.model.NavDrawerItem
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.model.NavMenuItem
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.model.NavMenuSection
 */
public abstract class AbstractNavDrawerActivity extends BaseActivity {

    private static final String LOG_TAG = AbstractNavDrawerActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private NavDrawerActivityConfiguration navConf;

    protected abstract NavDrawerActivityConfiguration getNavDrawerConfiguration();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navConf = getNavDrawerConfiguration();

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(navConf.getDrawerLayoutId());

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
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            if (getSupportFragmentManager().findFragmentById(R.id.left_drawer) == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.drawer_container, new LeftDrawerFragment())
                        .commit();
            }
            if (getSupportFragmentManager().findFragmentById(R.id.right_drawer) == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.drawer_container, new RightDrawerFragment())
                        .commit();
            }
        }
    }

    protected void initDrawerIcon() {
        setActionBarIcon(R.drawable.ic_drawer_menu);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (this.mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                this.mDrawerLayout.closeDrawer(Gravity.LEFT);
            } else {
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
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            return;
        }
        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
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

    public void onDrawerItemSelectionEvent(DrawerItemSelectionEvent event) {
        if (event.getMessage().getNavDrawerItem().updateActionBarTitle()) {
            setTitle(event.getMessage().getNavDrawerItem().getLabel());
        }

        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    public void openDrawer(final int gravity) {
        if (mDrawerLayout != null) {
            if (gravity != Gravity.LEFT && gravity != Gravity.RIGHT) {
                Log.d(LOG_TAG, String.format("No navigation drawer set for gravity %d", gravity));
            } else {
                final int anotherDrawer = gravity == Gravity.LEFT ? Gravity.RIGHT : Gravity.LEFT;
                if (mDrawerLayout.isDrawerOpen(anotherDrawer)) {
                    mDrawerLayout.closeDrawer(anotherDrawer);
                }
                mDrawerLayout.openDrawer(gravity);
            }
        }
    }

}