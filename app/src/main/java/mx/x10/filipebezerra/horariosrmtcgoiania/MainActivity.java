package mx.x10.filipebezerra.horariosrmtcgoiania;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import mx.x10.filipebezerra.horariosrmtcgoiania.utils.Operations;

/**
 * @author Filipe Bezerra
 * @since 1.0
 */
public class MainActivity extends SherlockFragmentActivity {

    private static WebView webView;

    private Menu menu;

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
        getSupportMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_fullscreen:
                setUpActionBarVisibility(item);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && ! getSupportActionBar().isShowing()) {
            setUpActionBarVisibility(menu.findItem(R.id.action_fullscreen));
            return true;
        }

        return super.onKeyUp(keyCode, event);
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
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (webView.canGoBack())) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public static class PlaceholderFragment extends SherlockFragment {

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

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            final String siteBrowsingMode = preferences.getString(resources.getString(R.string.pref_key_site_browsing_mode),
                    "");

            Operations.log(siteBrowsingMode);

            webView.loadUrl(siteBrowsingMode);

            return rootView;
        }

    }
}
