package mx.x10.filipebezerra.horariosrmtcgoiania.view.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.github.johnpersano.supertoasts.SuperCardToast;
import com.github.johnpersano.supertoasts.SuperToast;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.provider.SuggestionsProvider;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public class MainActivity extends BaseActivity {

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarIcon(R.drawable.ic_menu_white_24dp);
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);

        SuperCardToast.onRestoreState(savedInstanceState, this);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH == intent.getAction()) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            if (TextUtils.isDigitsOnly(query)) {
                searchView.setQuery(query, false);
                searchView.clearFocus();

                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                        SuggestionsProvider.AUTHORITY, SuggestionsProvider.MODE);
                suggestions.saveRecentQuery(query, null);
            } else {
                SuperCardToast toast = new SuperCardToast(this, SuperToast.Type.STANDARD);
                toast.setAnimations(SuperToast.Animations.FLYIN);
                toast.setDuration(SuperToast.Duration.LONG);
                toast.setBackground(SuperToast.Background.BLUE);
                toast.setTextSize(SuperToast.TextSize.MEDIUM);
                toast.setSwipeToDismiss(true);
                toast.setTouchToDismiss(true);
                toast.setIcon(SuperToast.Icon.Dark.INFO, SuperToast.IconPosition.LEFT);
                toast.setText(getResources().getString(R.string.non_digit_voice_search));
                toast.show();
            }
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SuperCardToast.onSaveState(outState);
    }
}
