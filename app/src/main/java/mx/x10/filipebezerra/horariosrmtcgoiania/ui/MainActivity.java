package mx.x10.filipebezerra.horariosrmtcgoiania.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.OperationsUtils;

/**
 * Tela principal do aplicativo.<br />
 * Nesta tela são carregados o web browser que usuário pesquisa e interage com os horários e têm
 * acesso às principais funcionalidades do aplicativo.
 *
 * @author Filipe Bezerra
 * @since 1.0
 */
public class MainActivity extends SherlockFragmentActivity
    implements AdapterView.OnItemClickListener {

    private static final String TAG_WEB_BROWSER_FRAGMENT = "web_browser_fragment";

    private static final String TAG_SLIDE_MENU_LIST_FRAGMENT = "slide_menu_list_fragment";

    private static final String KEY_ACTION_BAR_IS_SHOWING_STATE = "key_action_bar_visibility_state";

    private static final String STATE_MENUDRAWER = MainActivity.class.getName() + ".menuDrawer";

    private Menu mActionBarMenu;

    private MenuDrawer mMenuDrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new WebBrowserFragment(), TAG_WEB_BROWSER_FRAGMENT)
                    .commit();
        } else {
            if (savedInstanceState.containsKey(KEY_ACTION_BAR_IS_SHOWING_STATE)) {
                if (savedInstanceState.getBoolean(KEY_ACTION_BAR_IS_SHOWING_STATE)) {
                    getSupportActionBar().show();
                } else {
                    getSupportActionBar().hide();
                }
            }
        }

        setUpMenuDrawer();
    }

    private void setUpMenuDrawer() {
        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY, Position.LEFT,
                MenuDrawer.MENU_DRAG_WINDOW);
        mMenuDrawer.setContentView(R.layout.activity_main);
        mMenuDrawer.setMenuView(R.layout.slide_menu_frame);
        mMenuDrawer.setSlideDrawable(R.drawable.drawer);
        mMenuDrawer.setDrawerIndicatorEnabled(true);
        mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
        mMenuDrawer.getMenuView().setBackgroundColor(getResources().getColor(R.color.white));
        mMenuDrawer.setMenuSize(getResources().getInteger(R.integer.menu_drawer_size));

        SlideMenuListFragment menu = (SlideMenuListFragment)getSupportFragmentManager()
                .findFragmentById(R.id.f_menu);

        menu.getListView().setOnItemClickListener(this);

        mMenuDrawer.setOnDrawerStateChangeListener(new MenuDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMenuDrawer.openMenu(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.menu_global, menu);
        this.mActionBarMenu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_fullscreen:
                setUpActionBarVisibility(item);
                return true;

            case android.R.id.home:
                mMenuDrawer.toggleMenu();
                return true;
        }

        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_fullscreen).setVisible(! mMenuDrawer.isMenuVisible());
        return super.onPrepareOptionsMenu(menu);
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
        final int drawerState = mMenuDrawer.getDrawerState();

        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
            mMenuDrawer.closeMenu();
        } else if (! actionBarShowing) {
            setUpActionBarVisibility(mActionBarMenu.findItem(R.id.action_fullscreen));
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        mMenuDrawer.restoreState(inState.getParcelable(STATE_MENUDRAWER));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_ACTION_BAR_IS_SHOWING_STATE, getSupportActionBar().isShowing());
        outState.putParcelable(STATE_MENUDRAWER, mMenuDrawer.saveState());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String [] urlsServicosRmtc = getResources().getStringArray(R.array.servicos_rmtc_urls);

        if (i > (urlsServicosRmtc.length - 1))
            OperationsUtils.log(OperationsUtils.LogType.ERROR, "O índice [%d] não é válido " +
                    "ou não foi implementado!", i);
        else {
            Fragment webBrowserFragment = getSupportFragmentManager().findFragmentByTag(
                    TAG_WEB_BROWSER_FRAGMENT);

            ((WebBrowserFragment) webBrowserFragment).getWebView().loadUrl(urlsServicosRmtc[i]);

            mMenuDrawer.setActiveView(view);
            mMenuDrawer.closeMenu();
        }
    }
}
