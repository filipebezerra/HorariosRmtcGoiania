package mx.x10.filipebezerra.horariosrmtcgoiania.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
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

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.ConnectionDetector;
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

    private static final String KEY_ACTION_BAR_IS_SHOWING_STATE = "key_action_bar_visibility_state";
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
                .replace(R.id.menu_frame, new SlideMenuListFragment())
                .commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.menu_global, menu);
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
            setUpActionBarVisibility(menu.findItem(R.id.action_fullscreen));
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
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
            webView.loadUrl(urlsServicosRmtc[index]);
        }
    }

    public static class WebBrowserFragment extends SherlockFragment {

        private SmoothProgressBar progressBar;

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
            webView.setWebViewClient(new CustomWebViewClient());

            webView.setFocusableInTouchMode(true);
            webView.setClickable(true);

            progressBar = (SmoothProgressBar) rootView.findViewById(R.id.progressBar);
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

        private class CustomWebViewClient extends WebViewClient {

            private boolean errorWhenLoading = false;
            private boolean isInternetPresent = true;

            private void showNoInternetAccessDialog() {
                OperationsUtils.showAlertDialog(getActivity(), "Sem acesso à internet",
                        "Desculpe, mas você não está conectado à internet.", false,
                        R.drawable.ic_fail);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                errorWhenLoading = true;

                ConnectionDetector connectionDetector = new ConnectionDetector(getActivity()
                        .getApplicationContext());

                isInternetPresent = connectionDetector.isConnectingToInternet();

                if (!isInternetPresent) {
                    showNoInternetAccessDialog();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        }
    }
}
