package mx.x10.filipebezerra.horariosrmtcgoiania.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.SherlockFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.NetworkUtils;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.NotificationUtils;

/**
 * Classe UI (Fragment) responsável por renderizar as páginas no componente WebBrowser.
 *
 * @author Filipe Bezerra
 * @since 1.0
 */
public class WebBrowserFragment extends SherlockFragment {

    @InjectView(R.id.progressBar)
    SmoothProgressBar progressBar;

    @InjectView(R.id.webView)
    WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web_browser, container, false);
        ButterKnife.inject(this, rootView);

        setUpViews();

        return rootView;
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
        webView.setWebViewClient(new CustomWebViewClient());
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
            webView.loadUrl(getResources().getString(R.string.default_url));
        } else if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
        }
    }

    private class CustomWebViewClient extends WebViewClient {

        private boolean errorWhenLoading = false;
        private boolean isInternetPresent = true;

        private void showNoInternetAccessDialog() {
            NotificationUtils.showAlertDialog(getActivity(), "Sem acesso à internet",
                    "Desculpe, mas você não está conectado à internet.", false,
                    R.drawable.ic_fail);
        }

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
