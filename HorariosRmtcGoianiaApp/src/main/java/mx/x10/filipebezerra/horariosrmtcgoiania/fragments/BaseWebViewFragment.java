package mx.x10.filipebezerra.horariosrmtcgoiania.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import butterknife.ButterKnife;
import butterknife.InjectView;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.activities.BaseActivity;
import mx.x10.filipebezerra.horariosrmtcgoiania.utils.AndroidUtils;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.helpers.SnackBarHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.widgets.WebViewCompatSwipeRefreshLayout;
import timber.log.Timber;

/**
 * A fragment that displays a WebView.
 * <p>
 * The WebView is automically paused or resumed when the Fragment is paused or resumed.
 *
 * @author Filipe Bezerra
 * @version 2.1, 25/03/2015
 * @since 1.6
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.fragments.WebViewFragmentFactory
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.fragments.HorarioViagemFragment
 */
public class BaseWebViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = BaseWebViewFragment.class.getSimpleName();

    private static final String FILE_ASSET_OFFLINE_HTML = "file:///android_asset/www/offline.html";

    private boolean mHasInternetConnection = true;

    private boolean mIsWebViewAvailable;

    @InjectView(R.id.webView) protected WebView mWebView;

    @InjectView(R.id.swipe_refresh_layout) protected SwipeRefreshLayout mSwipeRefreshLayout;

    public static final String ARG_PARAM_URL_PAGE = BaseWebViewFragment.class.getSimpleName()
            + ".ARG_PARAM_URL_PAGE";

    /**
     * Handles the user interation with the page, errors and fire callbacks to pre and pos
     * loading each page.
     */
    private class CustomWebViewClient extends WebViewClient {
        private final String TAG = CustomWebChromeClient.class.getSimpleName();

        private CustomWebViewClient() {
            Timber.tag(TAG);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (isRmtcWebSite(url))
                return false;

            Timber.e(String.format(
                    getString(R.string.log_event_debug), "shouldOverrideUrlLoading", url,
                    "loading a non RMTC web site"));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (isInternetConnectionAvailable()) {
                onWebViewPageStarted();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (mHasInternetConnection) {
                onWebViewPageFinished();
            } else {
                onWebViewOfflinePageLoaded();
            }
        }

        @Override
        public void onReceivedError(final WebView view, final int errorCode,
                final String description, final String failingUrl) {
            Timber.e(String.format(
                    getString(R.string.log_event_error_network_request),
                    errorCode + ": " + description, "onReceivedError", mWebView.getOriginalUrl(),
                    failingUrl));
        }

        /**
         * Checks if the url loading in the #mWebView is a rmtc web site.
         *
         * @param url url loading.
         * @return if the loading url is a rmtc web site.
         */
        private boolean isRmtcWebSite(@NonNull final String url) {
            String loadingHost = Uri.parse(url).getHost();

            String rmtcWapHost = Uri.parse(getString(R.string.url_rmtc_wap)).getHost();
            String rmtcMobileHost = Uri.parse(getString(R.string.url_rmtc_horario_viagem)).getHost();

            return rmtcWapHost.equals(loadingHost) || rmtcMobileHost.equals(loadingHost);
        }
    }

    /**
     * Handles and presents to the user Javascript alerts fired in the host web page.
     */
    private class CustomWebChromeClient extends WebChromeClient {
        private final String TAG = CustomWebChromeClient.class.getSimpleName();

        private CustomWebChromeClient() {
            Timber.tag(TAG);
        }

        /**
         * Displays the alerts to the user when something is wrong, i.e., a field value is
         * required, the typed value is not valid or is not recognized.
         */
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Timber.d(String.format(
                    getString(R.string.log_event_debug), "onResponse", url, message));
            SnackBarHelper.show(getActivity(), message);
            result.confirm();
            return true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag(TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return setupContentView(inflater.inflate(R.layout.fragment_browser, container, false));
    }

    /**
     * Loads the url page passed to some of the helper constructors or restores the url
     * from the saved state.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isInternetConnectionAvailable()) {
            if (savedInstanceState == null) {
                mWebView.loadUrl(getArgUrlPage());
                Timber.d(String.format(
                        getString(R.string.log_event_debug), "onViewCreated", getArgUrlPage(),
                        "web page loaded from arguments"));
            } else {
                mWebView.restoreState(savedInstanceState);
                Timber.d(String.format(
                        getString(R.string.log_event_debug), "onViewCreated",
                        savedInstanceState.toString(),
                        "web page loaded from WebView saved state"));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    /**
     * Called when the fragment is no longer resumed. Pauses the WebView.
     */
    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.onPause();
        }
    }

    /**
     * Called when the fragment is visible to the user and actively running. Resumes the WebView.
     */
    @Override
    public void onResume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.onResume();
        }
        super.onResume();
    }

    /**
     * Called when the WebView has been detached from the fragment.
     * The WebView is no longer available after this time.
     */
    @Override
    public void onDestroyView() {
        mIsWebViewAvailable = false;
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    /**
     * Called when the fragment is no longer in use. Destroys the internal state of the WebView.
     */
    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        initiateReloading();
    }

    /**
     * Sets up all child views inside this root view.
     */
    @SuppressLint("SetJavaScriptEnabled")
    protected View setupContentView(final View fragmentView) {
        if (mWebView != null) {
            mWebView.destroy();
        }

        ButterKnife.inject(this, fragmentView);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);

        mWebView.setWebChromeClient(new CustomWebChromeClient());
        mWebView.setWebViewClient(new CustomWebViewClient());
        mWebView.requestFocus(View.FOCUS_DOWN);
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!view.hasFocus()) {
                            view.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });
        mIsWebViewAvailable = true;

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(
                    R.color.navdrawer_favorites_section_color,
                    R.color.navdrawer_wap_section_color,
                    R.color.navdrawer_horario_viagem_section_color,
                    R.color.navdrawer_planeje_sua_viagem_section_color,
                    R.color.navdrawer_ponto_a_ponto_section_color,
                    R.color.navdrawer_sac_section_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            if (mSwipeRefreshLayout instanceof WebViewCompatSwipeRefreshLayout) {
                WebViewCompatSwipeRefreshLayout compatSwipe = (WebViewCompatSwipeRefreshLayout)
                        mSwipeRefreshLayout;
                compatSwipe.setCanChildScrollUpCallback(
                        new WebViewCompatSwipeRefreshLayout.CanChildScrollUpCallback() {
                            @Override
                            public boolean canSwipeRefreshChildScrollUp() {
                                return mWebView != null && mWebView.getScrollY() > 0;
                            }
                        });
            }
        }

        return fragmentView;
    }

    /**
     * Callback when webview finishes loading a web page, validate the child views here.
     */
    protected void onWebViewPageFinished() {
        setSwipeRefreshing(false);
    }

    /**
     * Callback when webview starts loading a web page, invalidade the child views here.
     */
    protected void onWebViewPageStarted() {
        setSwipeRefreshing(true);
    }

    protected void onWebViewOfflinePageLoaded() {
        setSwipeRefreshing(false);
    }

    /**
     * Returns the url page passed to arguments in the helper constructor.
     *
     * @return url page passed to arguments
     */
    @NonNull
    protected String getArgUrlPage() {
        return getArguments().getString(ARG_PARAM_URL_PAGE);
    }

    protected void setSwipeRefreshing(final boolean refreshing) {
        if (mSwipeRefreshLayout != null ) {
            mSwipeRefreshLayout.setRefreshing(refreshing);
        }
    }

    protected void initiateReloading() {
        if (!isInternetConnectionAvailable()) {
            setSwipeRefreshing(false);
        } else {
            if (FILE_ASSET_OFFLINE_HTML.equals(mWebView.getUrl()))
                mWebView.loadUrl(getArgUrlPage());
            else
                mWebView.reload();
        }
    }

    protected void loadOfflinePage() {
        if (! FILE_ASSET_OFFLINE_HTML.equals(mWebView.getUrl()))
            mWebView.loadUrl(FILE_ASSET_OFFLINE_HTML);

        onWebViewOfflinePageLoaded();
    }

    protected boolean isInternetConnectionAvailable() {
        FragmentActivity drawerActivity = getActivity();
        if (AndroidUtils.checkAndNotifyNetworkState(drawerActivity,
                ((BaseActivity) drawerActivity).getFabMenu())) {
            Timber.d(String.format(
                    getString(R.string.log_event_debug), "isInternetConnectionAvailable",
                    mWebView.getUrl(), "no internet connectivity available"));
            mHasInternetConnection = false;
            loadOfflinePage();
            return false;
        } else {
            mHasInternetConnection = true;
            return true;
        }
    }

    /**
     * Gets the WebView.
     */
    public WebView getWebView() {
        return mIsWebViewAvailable ? mWebView : null;
    }
}