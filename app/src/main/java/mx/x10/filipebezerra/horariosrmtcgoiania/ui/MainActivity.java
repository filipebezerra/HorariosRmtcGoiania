package mx.x10.filipebezerra.horariosrmtcgoiania.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

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
    implements SlideMenuListFragment.OnSlideMenuItemSelectedListener {

    private static final String TAG_WEB_BROWSER_FRAGMENT = "web_browser_fragment";

    private static final String TAG_SLIDE_MENU_LIST_FRAGMENT = "slide_menu_list_fragment";

    private static final String KEY_ACTION_BAR_IS_SHOWING_STATE = "key_action_bar_visibility_state";

    private Menu actionBarMenu;

    private SlidingMenu slideMenu;

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

        slideMenu = new SlidingMenu(this);
        slideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slideMenu.setShadowWidth(15);
        slideMenu.setShadowDrawable(R.drawable.shadow);
        slideMenu.setBehindOffset(60);
        slideMenu.setFadeEnabled(true);
        slideMenu.setFadeDegree(0.35f);
        slideMenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                invalidateOptionsMenu();
                getSupportActionBar().setTitle(R.string.slide_menu_title_opened);
            }
        });
        slideMenu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
            @Override
            public void onClose() {
                invalidateOptionsMenu();
                getSupportActionBar().setTitle(R.string.slide_menu_title_closed);
            }
        });
        slideMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slideMenu.setMenu(R.layout.slide_menu_frame);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, new SlideMenuListFragment(), TAG_SLIDE_MENU_LIST_FRAGMENT)
                .commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.menu_global, menu);
        this.actionBarMenu = menu;

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
                slideMenu.toggle(true);
                return true;
        }

        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_fullscreen).setVisible(! slideMenu.isMenuShowing());
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
        boolean actionBarShowing = getSupportActionBar().isShowing();

        if (slideMenu.isMenuShowing()) {
            slideMenu.toggle(true);
        } else if (! actionBarShowing) {
            setUpActionBarVisibility(actionBarMenu.findItem(R.id.action_fullscreen));
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_ACTION_BAR_IS_SHOWING_STATE, getSupportActionBar().isShowing());
    }

    @Override
    public void onItemSelected(int index) {
        String [] urlsServicosRmtc = getResources().getStringArray(R.array.servicos_rmtc_urls);

        if (index > (urlsServicosRmtc.length - 1))
            OperationsUtils.log(OperationsUtils.LogType.ERROR, "O índice [%d] não é válido " +
                    "ou não foi implementado!", index);
        else {
            slideMenu.toggle(true);
            Fragment webBrowserFragment = getSupportFragmentManager().findFragmentByTag(
                    TAG_WEB_BROWSER_FRAGMENT);

            ((WebBrowserFragment) webBrowserFragment).getWebView().loadUrl(urlsServicosRmtc[index]);
        }
    }

}
