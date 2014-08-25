package mx.x10.filipebezerra.horariosrmtcgoiania.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import de.psdev.licensesdialog.LicensesDialogFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.ConstantsUtils;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.LogUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Tela principal do aplicativo.<br />
 * Nesta tela são carregados o web browser que usuário pesquisa e interage com os horários e têm
 * acesso às principais funcionalidades do aplicativo.
 *
 * @author Filipe Bezerra
 * @since 1.0
 */
public class MainActivity extends SherlockFragmentActivity
    implements AdapterView.OnItemClickListener, ConstantsUtils {

    private static final String TAG_WEB_BROWSER_FRAGMENT = "web_browser_fragment";

    private static final String TAG_SLIDE_MENU_LIST_FRAGMENT = "slide_menu_list_fragment";

    private static final String KEY_ACTION_BAR_VISIBILITY_STATE = "action_bar_visibility_state";

    private static final String KEY_SLIDE_MENU_STATE = "slide_menu_state";

    private Menu mMenu;

    private MenuDrawer mSlideMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new WebBrowserFragment(), TAG_WEB_BROWSER_FRAGMENT)
                    .commit();
        } else {
            if (savedInstanceState.containsKey(KEY_ACTION_BAR_VISIBILITY_STATE)) {
                if (savedInstanceState.getBoolean(KEY_ACTION_BAR_VISIBILITY_STATE)) {
                    getSupportActionBar().show();
                } else {
                    getSupportActionBar().hide();
                }
            }
        }

        setUpSlideMenu();
        mSlideMenu.openMenu(true);
    }

    private void setUpSlideMenu() {
        mSlideMenu = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY, Position.LEFT,
                MenuDrawer.MENU_DRAG_CONTENT);
        mSlideMenu.setContentView(R.layout.activity_main);
        mSlideMenu.setMenuView(R.layout.slide_menu_frame);
        mSlideMenu.setSlideDrawable(R.drawable.drawer);
        mSlideMenu.setDrawerIndicatorEnabled(true);
        mSlideMenu.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
        mSlideMenu.setMenuSize(getResources().getInteger(R.integer.menu_drawer_size));

        SlideMenuListFragment menu = (SlideMenuListFragment)getSupportFragmentManager()
                .findFragmentById(R.id.f_menu);

        menu.getListView().setOnItemClickListener(this);

        mSlideMenu.setOnDrawerStateChangeListener(new MenuDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {

                if (newState == MenuDrawer.STATE_OPEN) {
                    getSupportActionBar().setTitle(getResources().getString(R.string
                            .slide_menu_title_opened));
                } else if (newState == MenuDrawer.STATE_CLOSED) {
                    getSupportActionBar().setTitle(getResources().getString(R.string
                            .slide_menu_title_closed));
                }

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.menu_global, menu);
        this.mMenu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mSlideMenu.toggleMenu();
                return true;

            case R.id.action_fullscreen:
                setUpActionBarVisibility(item);
                return true;

            case R.id.action_notices:
                LicensesDialogFragment licensesDialogFragment = LicensesDialogFragment
                        .newInstance(R.raw.notices, false);
                licensesDialogFragment.show(getSupportFragmentManager(), null);
                break;
        }

        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final boolean slideMenuIsShowing = mSlideMenu.isMenuVisible();

        for(int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(! slideMenuIsShowing);
        }

        return true;
    }

    private void setUpActionBarVisibility(final MenuItem item) {
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar.isShowing()) {
            actionBar.hide();
            item.setIcon(R.drawable.ic_action_return_from_full_screen);
        } else {
            actionBar.show();
            item.setIcon(R.drawable.ic_action_full_screen);
        }
    }

    @Override
    public void onBackPressed() {
        final boolean actionBarShowing = getSupportActionBar().isShowing();
        final int drawerState = mSlideMenu.getDrawerState();

        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
            mSlideMenu.closeMenu();
        } else if (! actionBarShowing) {
            setUpActionBarVisibility(mMenu.findItem(R.id.action_fullscreen));
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        mSlideMenu.restoreState(inState.getParcelable(KEY_SLIDE_MENU_STATE));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_ACTION_BAR_VISIBILITY_STATE, getSupportActionBar().isShowing());
        outState.putParcelable(KEY_SLIDE_MENU_STATE, mSlideMenu.saveState());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String [] urlsServicosRmtc = getResources().getStringArray(R.array.slide_menu_row_url);

        if (i > (urlsServicosRmtc.length - 1))
            LogUtils.log(LogUtils.LogType.ERROR, "O índice [%d] não é válido " +
                    "ou não foi implementado!", i);
        else {
            Fragment webBrowserFragment = getSupportFragmentManager().findFragmentByTag(
                    TAG_WEB_BROWSER_FRAGMENT);

            ((WebBrowserFragment) webBrowserFragment).webView.loadUrl(urlsServicosRmtc[i]);

            mSlideMenu.setActiveView(view);
            mSlideMenu.closeMenu(true);
        }
    }

    @Override
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }
}
