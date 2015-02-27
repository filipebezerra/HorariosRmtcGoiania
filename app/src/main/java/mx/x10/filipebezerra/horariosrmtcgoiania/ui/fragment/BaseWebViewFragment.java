package mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.NetworkUtils;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.SnackBarHelper;

/**
 * Base fragment for each loaded page inside the webview. Each page is served inside your own
 * fragment implementation.
 *
 * @author Filipe Bezerra
 * @version 2.0, 02/26/2015
 * @since 1.6
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.HorarioViagemFragment
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.PlanejeViagemFragment
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.PontoToPontoFragment
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.SacFragment
 */
public abstract class BaseWebViewFragment extends Fragment {

    @InjectView(R.id.progressBar)
    protected ProgressBarCircularIndeterminate mProgressBar;

    @InjectView(R.id.webView)
    protected WebView mWebView;

    @InjectView(R.id.floatButtonMarkFavorite)
    protected ButtonFloat mFloatButtonMarkFavorite;

    protected String mUrlToLoad;

    protected abstract String getUrlToLoad();

    protected void onWebViewPageStarted() {
        mProgressBar.setVisibility(View.VISIBLE);
        mFloatButtonMarkFavorite.setVisibility(View.GONE);
        mFloatButtonMarkFavorite.hide();
    }

    protected void onWebViewPageFinished() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public BaseWebViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrlToLoad = getUrlToLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_browser, container, false);
        ButterKnife.inject(this, rootView);
        loadBaseContentView();
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            mWebView.loadUrl(mUrlToLoad);
        } else {
            mWebView.restoreState(savedInstanceState);
        }
    }

    public WebView getWebView() {
        return mWebView;
    }

    public enum UrlPart {
        LAST_PATH_SEGMENT
    }

    public String getUrlPartFromCurrentUrl(UrlPart part) {
        switch (part) {
            case LAST_PATH_SEGMENT:
                return Uri.parse(mWebView.getUrl()).getLastPathSegment();
            default:
                return null;
        }
    }

    private void loadBaseContentView() {
        // Habilitando suporte JavaScript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);

        // Especifica o estilo das barras de rolagem.
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        // Define se as barras de rolagem vai desaparecer quando a view não estiver em rolagem.
        mWebView.setScrollbarFadingEnabled(true);

        // Define se esta view pode receber o foco no modo de toque.
        mWebView.setFocusableInTouchMode(true);

        // Ativa ou desativa eventos de clique para este view.
        mWebView.setClickable(true);

        // Habilitando o clique em links para serem abertos pela própria aplicação e não
        // pelo aplicativo browser padrão do dispositivo
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
    }

    private class CustomWebViewClient extends WebViewClient {

        private boolean errorWhenLoading = false;
        private boolean isInternetPresent = true;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            onWebViewPageStarted();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            errorWhenLoading = true;

            isInternetPresent = NetworkUtils.isConnectingToInternet(getActivity());

            if (!isInternetPresent) {
                SnackBarHelper.show(getActivity(), getString(R.string.no_internet_connectivity));
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            onWebViewPageFinished();
        }

    }

    private class CustomWebChromeClient extends WebChromeClient {

        public CustomWebChromeClient() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            SnackBarHelper.show(getActivity(), message);
            
            result.confirm();
            return true;
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            SnackBarHelper.show(getActivity(), consoleMessage.message());
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                if (mProgressBar.getVisibility() == View.VISIBLE) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        }
    }

}