package mx.x10.filipebezerra.horariosrmtcgoiania;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import mx.x10.filipebezerra.horariosrmtcgoiania.utils.Operations;

/**
 * @author Filipe Bezerra
 * @since 1.0
 */
public class MainActivity extends ActionBarActivity {

    private static WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (webView.canGoBack())) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);


            webView = (WebView) rootView.findViewById(R.id.webView);
            webView.getSettings().setAppCacheEnabled(true);

            // Habilitando suporte JavaScript
            webView.getSettings().setJavaScriptEnabled(true);

            // Habilitando controles de zoom
            webView.getSettings().setBuiltInZoomControls(true);

            // Habilitando o clique em links para serem abertos pela própria aplicação e não
            // pelo aplicativo browser padrão do dispositivo
            webView.setWebViewClient(new WebViewClient());

            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setGeolocationEnabled(true);

            final Resources resources = getActivity().getResources();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());;
            final String siteBrowsingMode = preferences.getString(resources.getString(R.string.pref_key_site_browsing_mode),
                    "");

            Operations.log(siteBrowsingMode);

            webView.loadUrl(siteBrowsingMode);

            return rootView;
        }

    }
}
