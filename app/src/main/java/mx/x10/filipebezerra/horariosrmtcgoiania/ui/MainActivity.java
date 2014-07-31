package mx.x10.filipebezerra.horariosrmtcgoiania.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
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

    private static WebView webView;

    private Menu menu;

    private SlidingMenu slideMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new WebBrowserFragment())
                    .commit();
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
        slideMenu.setMenu(R.layout.menu_frame);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, new SlideMenuListFragment())
                .commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_fullscreen:
                setUpActionBarVisibility(item);
                return true;
            case android.R.id.home: slideMenu.toggle(true);
                return true;
        }
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_fullscreen).setVisible(! slideMenu.isMenuShowing());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !getSupportActionBar().isShowing()) {
            setUpActionBarVisibility(menu.findItem(R.id.action_fullscreen));
            return true;
        }

        return false;
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            boolean actionBarShowing = getSupportActionBar().isShowing();

            if ((webView.canGoBack()) && actionBarShowing) {
                webView.goBack();
            } else if (slideMenu.isMenuShowing()) {
                slideMenu.toggle(true);
            } else if (! actionBarShowing) {
                setUpActionBarVisibility(menu.findItem(R.id.action_fullscreen));
            } else {
                super.onBackPressed();
            }

            return true;
        }

        return false;
    }

    @Override
    public void onItemSelected(int index) {
        String [] urlsServicosRmtc = getResources().getStringArray(R.array.servicos_rmtc_urls);

        if (index > (urlsServicosRmtc.length - 1))
            OperationsUtils.log(OperationsUtils.LogType.ERROR, "O índice [%d] não é válido " +
                    "ou não foi implementado!", index);
        else {
            slideMenu.toggle(true);
            webView.loadUrl(urlsServicosRmtc[index]);
        }
    }

    public static class WebBrowserFragment extends SherlockFragment {

        public WebBrowserFragment() {
        }

        private View rootView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_web_browser, container, false);
            this.rootView = rootView;

            setUpViews();

            return rootView;
        }

        private void setUpViews() {
            webView = (WebView) rootView.findViewById(R.id.webView);

            // Habilitando suporte JavaScript
            webView.getSettings().setJavaScriptEnabled(true);

            // Habilitando controles de zoom
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);

            // Configurações da ScrollBar
            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            webView.setScrollbarFadingEnabled(true);

            webView.getSettings().setLoadsImagesAutomatically(true);

            // Habilitando o clique em links para serem abertos pela própria aplicação e não
            // pelo aplicativo browser padrão do dispositivo
            webView.setWebViewClient(new WebViewClient());
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            webView.saveState(outState);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            if (savedInstanceState != null) {
                webView.restoreState(savedInstanceState);
            } else {
                ((SlideMenuListFragment.OnSlideMenuItemSelectedListener) getActivity())
                        .onItemSelected(0);
            }
        }
    }
}
