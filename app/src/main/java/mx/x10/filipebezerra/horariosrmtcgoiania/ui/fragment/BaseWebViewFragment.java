package mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import butterknife.ButterKnife;
import butterknife.InjectView;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.LogUtils;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.SnackBarHelper;

import static mx.x10.filipebezerra.horariosrmtcgoiania.util.LogUtils.LOGD;

/**
 * A fragment that displays a WebView.
 * <p>
 * The WebView is automically paused or resumed when the Fragment is paused or resumed.
 *
 * @author Filipe Bezerra
 * @version 2.0, 08/03/2015
 * @since 1.6
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.WebViewFragmentFactory
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.HorarioViagemFragment
 */
public class BaseWebViewFragment extends Fragment {
    private static final String LOG_TAG = BaseWebViewFragment.class.getSimpleName();

    public static final String ARG_PARAM_URL_PAGE = BaseWebViewFragment.class.getSimpleName()
            + "ARG_PARAM_URL_PAGE";

    @InjectView(R.id.webView) protected WebView mWebView;

    @InjectView(R.id.progressBar) protected ProgressBarCircularIndeterminate mProgressBar;

    @NonNull protected Activity mAttachedActivity;

    private boolean mIsWebViewAvailable;

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        mAttachedActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return setUpContentView(inflater.inflate(R.layout.fragment_browser, container, false));
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setRetainInstance(true);
    }

    /**
     * Loads the url page passed to some of the helper constructors or restores the url
     * from the saved state.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            mWebView.loadUrl(getArgUrlPage());
            LOGD(LOG_TAG, String.format(
                    getString(R.string.log_event_debug), "onViewCreated", mWebView.getUrl(),
                    "web page loaded from arguments"));
        } else {
            mWebView.restoreState(savedInstanceState);
            LOGD(LOG_TAG, String.format(
                    getString(R.string.log_event_debug), "onViewCreated", savedInstanceState.toString(),
                    "web page loaded from WebView saved state"));
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

    /**
     * Gets the WebView.
     */
    public WebView getWebView() {
        return mIsWebViewAvailable ? mWebView : null;
    }

    /**
     * Sets up all child views inside this root view.
     */
    @SuppressLint("SetJavaScriptEnabled")
    protected View setUpContentView(final View fragmentView) {
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

        return fragmentView;
    }

    /**
     * Returns the url page passed to arguments in the helper constructor.
     *
     * @return url page passed to arguments
     */
    @NonNull
    public String getArgUrlPage() {
        return getArguments().getString(ARG_PARAM_URL_PAGE);
    }

    /**
     * Callback when webview finishes loading a web page, validate the child views here.
     */
    protected void onWebViewPageFinished() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Callback when webview starts loading a web page, invalidade the child views here.
     */
    protected void onWebViewPageStarted() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setBackgroundColor(((MaterialNavigationDrawer) mAttachedActivity)
                    .getCurrentSection().getSectionColor());
        }
    }

    /**
     * Handles the user interation with the page, errors and fire callbacks to pre and pos
     * loading each page.
     */
    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (isRmtcWebSite(url)) {
                return false;
            }
            LOGD(LOG_TAG, String.format(
                    getString(R.string.log_event_debug), "shouldOverrideUrlLoading", url,
                    "loading a non RMTC web site"));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            onWebViewPageStarted();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            onWebViewPageFinished();
        }

        @Override
        public void onReceivedError(final WebView view, final int errorCode,
                                    final String description, final String failingUrl) {
            LogUtils.LOGE(LOG_TAG, String.format(
                    getString(R.string.log_event_error_network_request),
                    errorCode+": "+description, "onReceivedError", mWebView.getOriginalUrl(),
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
        /**
         * Displays the alerts to the user when something is wrong, i.e., a field value is
         * required, the typed value is not valid or is not recognized.
         */
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            LOGD(LOG_TAG, String.format(
                    getString(R.string.log_event_debug), "onResponse", url, message));
            SnackBarHelper.show(mAttachedActivity, message);
            result.confirm();
            return true;
        }
    }
}