package mx.x10.filipebezerra.horariosrmtcgoiania.view.fragment;

import android.graphics.Bitmap;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.NetworkUtils;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.ToastHelper;

/**
 * Classe UI (Fragment) base responsável por renderizar as páginas no componente WebBrowser. Deve
 * ser classe uma classe especializada para implementação de novas páginas.
 *
 * @author Filipe Bezerra
 * @since 1.6
 */
public abstract class BaseWebViewFragment extends Fragment {

    @InjectView(R.id.progressBar)
    protected SmoothProgressBar progressBar;

    @InjectView(R.id.webView)
    protected WebView webView;

    protected String urlToLoad;

    protected View rootView;

    public BaseWebViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        urlToLoad = getUrlToLoad();
    }

    protected abstract String getUrlToLoad();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_browser, container, false);
        ButterKnife.inject(this, rootView);
        setUpViews();
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            webView.loadUrl(urlToLoad);
        } else {
            webView.restoreState(savedInstanceState);
        }
    }

    private void setUpViews() {
        // Habilitando suporte JavaScript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);

        // Especifica o estilo das barras de rolagem.
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        // Define se as barras de rolagem vai desaparecer quando a view não estiver em rolagem.
        webView.setScrollbarFadingEnabled(true);

        // Define se esta view pode receber o foco no modo de toque.
        webView.setFocusableInTouchMode(true);

        // Ativa ou desativa eventos de clique para este view.
        webView.setClickable(true);

        // Habilitando o clique em links para serem abertos pela própria aplicação e não
        // pelo aplicativo browser padrão do dispositivo
        webView.setWebChromeClient(new CustomWebChromeClient());
        webView.setWebViewClient(new CustomWebViewClient());

        webView.requestFocus(View.FOCUS_DOWN);
        webView.setOnTouchListener(new View.OnTouchListener() {
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

    public WebView getWebView() {
        return webView;
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
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            errorWhenLoading = true;

            isInternetPresent = NetworkUtils.isConnectingToInternet(getActivity());

            if (!isInternetPresent) {
                new ToastHelper(getActivity()).showWarning(getResources().getString(
                        R.string.no_internet_connectivity));
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
            }
        }

    }

    private class CustomWebChromeClient extends WebChromeClient {

        public CustomWebChromeClient() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            new ToastHelper(getActivity()).showInformation(message);
            result.confirm();
            return true;
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            new ToastHelper(getActivity()).showInformation(consoleMessage.message());
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        }
    }

}