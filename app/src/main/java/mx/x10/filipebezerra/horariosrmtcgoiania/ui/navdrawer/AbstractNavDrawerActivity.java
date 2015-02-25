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
import mx.x10.filipebezerra.horariosrmtcgoiania.event.NavigationDrawerSelectionEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.activity.BaseActivity;

/**
 * @author Filipe Bezerra
 *         Michenux (http://www.michenux.net/android-navigation-drawer-748.html)
 * @since 2.0
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
                getSupportActionBar().setSubtitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setSubtitle(null);
                invalidateOptionsMenu();
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
        } if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)){
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

    public void onNavigationDrawerSelectionEvent(NavigationDrawerSelectionEvent event) {
        final int position = event.getPosition();
        final int gravity = event.getGravity();

        if (gravity == Gravity.LEFT) {
            if (event.isUpdatable()) {
                setTitle(event.getDescription());
            }
        } else if (gravity != Gravity.RIGHT) {
            Log.d(LOG_TAG, String.format("No navigation drawer set for gravity %d", gravity));
        }

        if (mDrawerLayout.isDrawerOpen(gravity)) {
            mDrawerLayout.closeDrawer(gravity);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setSubtitle(mTitle);
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