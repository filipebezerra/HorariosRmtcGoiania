package mx.x10.filipebezerra.horariosrmtcgoiania.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.network.RequestQueueManager;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.HorarioViagemFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.SnackBarHelper;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 2.0, 02/27/2015
 * @since #
 */
public class HomeActivity extends BaseActivity {

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();

    private SearchView mSearchView;

    private MenuItem mSearchMenuItem;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleSearchQuery(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchMenuItem);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconifiedByDefault(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case R.id.action_share:
                startActivity(Intent.createChooser(createShareIntent(),
                        getString(R.string.share_dialog_title)));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            if (MenuItemCompat.expandActionView(mSearchMenuItem)) {
                mSearchView.requestFocus();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @SuppressWarnings("deprecation")
    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        } else {
            shareIntent.addFlags(shareIntent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
        return shareIntent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mConnectionReceiver, new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION));

        // TODO : this is the callback handle search (comes from GlobalSearch configuration)
        handleSearchQuery(getIntent());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mConnectionReceiver);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void handleSearchQuery(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            onSearch(intent);
        }
    }

    private void onSearch(Intent intent) {
        final String query = intent.getStringExtra(SearchManager.QUERY);

        if (TextUtils.isDigitsOnly(query)) {
            mSearchView.clearFocus();
            mSearchView.setQuery(query, false);

            String url = Uri.parse("http://m.rmtcgoiania.com.br/horariodeviagem/validar")
                    .buildUpon().appendQueryParameter("txtNumeroPonto", query)
                    .build().toString();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String status = response.getString("status");

                                if ("sucesso".equals(status)) {
                                    Bundle arguments = new Bundle();
                                    arguments.putString(
                                            HorarioViagemFragment.ARG_PARAM_BUS_STOP_NUMBER, query);

                                    // TODO : Send query to {@link HorarioViagemFragment}
                                    getSectionByTitle(getString(
                                            R.string.navdrawer_menu_item_rmtc_horarios_viagem)).select();
                                } else {
                                    SnackBarHelper.show(HomeActivity.this, response.getString("mensagem"));
                                }
                            } catch (JSONException e) {
                                Log.e(LOG_TAG, String.format("Erro no parsing de %s",
                                        response.toString()), e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(LOG_TAG, String.format("Erro na requisição de %s", query),
                                    error);
                            SnackBarHelper.show(HomeActivity.this,
                                    "Não foi possível fazer a busca. Por favor, Tente novamente!");
                        }
                    }
            );

            RequestQueueManager.getInstance(HomeActivity.this).addToRequestQueue(request, LOG_TAG);
        } else {
            SnackBarHelper.show(HomeActivity.this, getString(R.string.non_digit_voice_search));
        }
    }

    // TODO : handle notificationMessage bus event
    /**
    @Subscribe
    public void onNotificationEvent(NotificationMessage message) {
        MaterialSection section = getSectionByTitle(getString(
                R.string.navdrawer_menu_item_favorite_bus_stops));
        int actual = section.getNotifications();

        switch (message.getmNotificationType()) {
            case START:
                section.setNotifications(1);
                break;
            case INCREMENT:
                section.setNotifications(actual++);
                break;
            case DECREMENT:
                section.setNotifications(actual--);
                break;
            case RESET:
                section.setNotifications(0);
                break;
        }
    }
    **/
}