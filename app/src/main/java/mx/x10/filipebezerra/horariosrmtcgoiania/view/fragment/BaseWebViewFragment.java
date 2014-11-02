package mx.x10.filipebezerra.horariosrmtcgoiania.view.fragment;

import android.annotation.SuppressLint;
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
import android.webkit.WebView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.ToastHelper;

/**
 * Classe UI (Fragment) base responsável por renderizar as páginas no componente WebBrowser. Deve
 * ser classe uma classe especializada para implementação de novas páginas.
 *
 * @author Filipe Bezerra
 * @since 1.6
 */
public class BaseWebViewFragment extends Fragment {

    private static final String ARG_PARAM_URL_TO_LOAD = "PARAM_URL_TO_LOAD";

    protected String urlToLoad;
    protected View rootView;

    @InjectView(R.id.progressBar)
    protected SmoothProgressBar progressBar;

    @InjectView(R.id.webView)
    protected WebView webView;

    public BaseWebViewFragment() {
    }

    @SuppressLint("ValidFragment")
    public BaseWebViewFragment(String urlToLoad) {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_URL_TO_LOAD, urlToLoad);
        setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if (args != null) {
            urlToLoad = args.getString(ARG_PARAM_URL_TO_LOAD);
        }
    }

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
        webView.getSettings().setJavaScriptEnabled(true);

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

    private class CustomWebChromeClient extends WebChromeClient {

        public CustomWebChromeClient() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            new ToastHelper(getActivity()).showGeneralAlert(message);
            result.confirm();
            return true;
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            new ToastHelper(getActivity()).showGeneralAlert(consoleMessage.message());
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