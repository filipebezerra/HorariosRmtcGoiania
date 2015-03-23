package mx.x10.filipebezerra.horariosrmtcgoiania.activities;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.fragments.FavoriteBusStopListFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.fragments.HorarioViagemFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.fragments.WebViewFragmentFactory;
import mx.x10.filipebezerra.horariosrmtcgoiania.managers.DaoManager;
import mx.x10.filipebezerra.horariosrmtcgoiania.managers.RequestQueueManager;
import mx.x10.filipebezerra.horariosrmtcgoiania.managers.SuggestionsProviderManager;
import mx.x10.filipebezerra.horariosrmtcgoiania.utils.AndroidUtils;
import mx.x10.filipebezerra.horariosrmtcgoiania.utils.NetworkUtils;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.events.EventBusProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.helpers.ProgressDialogHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.helpers.SnackBarHelper;
import org.json.JSONException;
import org.json.JSONObject;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static mx.x10.filipebezerra.horariosrmtcgoiania.fragments.WebViewFragmentFactory.buildFinalUrl;
import static mx.x10.filipebezerra.horariosrmtcgoiania.fragments.WebViewFragmentFactory.buildHorarioViagemUrl;
import static mx.x10.filipebezerra.horariosrmtcgoiania.fragments.WebViewFragmentFactory.newHorarioViagemPageFragment;
import static mx.x10.filipebezerra.horariosrmtcgoiania.fragments.WebViewFragmentFactory.newPlanejeViagemPageFragment;
import static mx.x10.filipebezerra.horariosrmtcgoiania.fragments.WebViewFragmentFactory.newPontoaPontoPageFragment;
import static mx.x10.filipebezerra.horariosrmtcgoiania.fragments.WebViewFragmentFactory.newSacPageFragment;
import static mx.x10.filipebezerra.horariosrmtcgoiania.fragments.WebViewFragmentFactory.newWapPageFragment;

/**
 * Activity base containing based implementation of Navigation Drawer and all application base
 * behavior.
 *
 * @author Filipe Bezerra
 * @version 2.0, 08/03/2015
 * @since #
 */
public abstract class BaseActivity extends MaterialNavigationDrawer {

    private static final String TAG = BaseActivity.class.getSimpleName();
    /**
     * Search handled over all application.
     */
    protected SearchView mSearchView;

    /**
     * Search menu item reference.
     *
     * @see #onCreateOptionsMenu
     */
    protected MenuItem mSearchMenuItem;

    /**
     * Broadcast Receiver to detect and notify internet connection issues.
     */
    protected BroadcastReceiver mConnectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            NetworkUtils.checkAndNotifyNetworkState(context);
        }
    };

    /**
     * Navigation drawer section of {@link mx.x10.filipebezerra.horariosrmtcgoiania.fragments.HorarioViagemFragment}
     * used in {@link #searchStopCode} to perform user search requests.
     */
    protected MaterialSection horarioViagemSection;

    /**
     * Navigation drawer section of {@link mx.x10.filipebezerra.horariosrmtcgoiania.fragments.FavoriteBusStopListFragment}.
     */
    protected MaterialSection favoriteBusStopSection;

    /**
     * The delegation method that initializes the activity. Don't use activity's onCreate method.
     */
    @Override
    public void init(final Bundle savedInstanceState) {
        setUpHeader();
        addPrimarySections();
        addSecondarySections();
        addBottomSections();
        setBackPattern(MaterialNavigationDrawer.BACKPATTERN_BACK_TO_FIRST);

        // Can come from Global search, refer to searchable.xml
        handleSearchQuery(getIntent());
    }

    /**
     * Setup the drawer header such as the image if drawerType is DRAWERHEADER_IMAGE or {@link
     * it.neokree.materialnavigationdrawer.elements.MaterialAccount} if drawerType is
     * DRAWERHEADER_ACCOUNTS.
     */
    private void setUpHeader() {
        setDrawerHeaderImage(R.drawable.drawer_header);
        addAccountSections();
    }

    /**
     * Setup the user account
     */
    private void addAccountSections() {
        // TODO : reserved for future implementation
    }

    /**
     * Dynamical sections, according with user preferences
     */
    @SuppressWarnings("unchecked")
    private void addPrimarySections() {
        addSection(favoriteBusStopSection = newSection(
                getString(R.string.navdrawer_section_favorite_bus_stops),
                R.drawable.ic_drawer_pontos_favoritos, new FavoriteBusStopListFragment())
                .setNotifications(getFavoriteCount())
                .setSectionColor(
                        getColor(R.color.navdrawer_favorites_section_color),
                        getColor(R.color.navdrawer_favorites_section_dark_color)));
    }

    /**
     * Convenient method for return a color integer from the application's package's default color
     * table.
     *
     * @param resId The desired resource identifier, must be a color identifier.
     * @return Resource id for the color
     */
    public final int getColor(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    /**
     * Statical sections, containing predefined sections
     */
    @SuppressWarnings("unchecked")
    private void addSecondarySections() {
        addSubheader(getString(R.string.navdrawer_sub_section_horario_viagem));
        addSection(newSection(getString(R.string.navdrawer_section_rmtc_wap),
                R.drawable.ic_drawer_wap,
                newWapPageFragment(BaseActivity.this))
                .setSectionColor(
                        getColor(R.color.navdrawer_wap_section_color),
                        getColor(R.color.navdrawer_wap_section_dark_color)));
        addSection(horarioViagemSection = newSection(
                getString(R.string.navdrawer_section_rmtc_horario_viagem),
                R.drawable.ic_drawer_horario_viagem,
                newHorarioViagemPageFragment(BaseActivity.this))
                .setSectionColor(
                        getColor(R.color.navdrawer_horario_viagem_section_color),
                        getColor(R.color.navdrawer_horario_viagem_section_dark_color)));

        addDivisor();

        addSection(newSection(getString(R.string.navdrawer_section_rmtc_planeje_viagem),
                R.drawable.ic_drawer_planeje_sua_viagem,
                newPlanejeViagemPageFragment(BaseActivity.this))
                .setSectionColor(
                        getColor(R.color.navdrawer_planeje_sua_viagem_section_color),
                        getColor(R.color.navdrawer_planeje_sua_viagem_section_dark_color)));
        addSection(newSection(getString(R.string.navdrawer_section_rmtc_ponto_a_ponto),
                R.drawable.ic_drawer_ponto_a_ponto,
                newPontoaPontoPageFragment(BaseActivity.this))
                .setSectionColor(
                        getColor(R.color.navdrawer_ponto_a_ponto_section_color),
                        getColor(R.color.navdrawer_ponto_a_ponto_section_dark_color)));
        addSection(newSection(getString(R.string.navdrawer_section_rmtc_sac),
                R.drawable.ic_drawer_sac,
                newSacPageFragment(BaseActivity.this))
                .setSectionColor(
                        getColor(R.color.navdrawer_sac_section_color),
                        getColor(R.color.navdrawer_sac_section_dark_color)));
    }

    /**
     * Statical sections, containing specific app definitions and help content
     */
    @SuppressWarnings("unchecked")
    private void addBottomSections() {
        // TODO : Reserved for future implementation - issue #66
        /*
        addBottomSection(newSection(getString(R.string.navdrawer_fixed_menu_item_help),
                R.drawable.ic_drawer_help, ...));
                */

        addBottomSection(newSection(getString(R.string.action_share), R.drawable.ic_share,
                Intent.createChooser(createShareIntent(),
                        getString(R.string.share_dialog_title))));

        addBottomSection(newSection(getString(R.string.navdrawer_bottom_section_configurations),
                R.drawable.ic_drawer_settings,
                new Intent(BaseActivity.this, SettingsActivity.class)));
    }

    /**
     * Returns the favorite count retrieve from sqlite.
     *
     * @return Favorite count.
     */

    private int getFavoriteCount() {
        return (int) DaoManager.getInstance(BaseActivity.this).getFavoriteBusStopDao().count();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mConnectionReceiver, new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION));
        EventBusProvider.getInstance().getEventBus().register(BaseActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mConnectionReceiver);
        EventBusProvider.getInstance().getEventBus().unregister(BaseActivity.this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleSearchQuery(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_global, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchMenuItem);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    /**
     * Creates the share intent.
     *
     * @return share intent
     */
    @SuppressWarnings("deprecation")
    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        } else {
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
        return shareIntent;
    }

    /**
     * Inject custom font into {@link Context}.
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     * Handles search hardware button. Compatibility for old Android devices.
     */
    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            if (MenuItemCompat.expandActionView(mSearchMenuItem)) {
                mSearchView.requestFocus();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * Handles the menu hardware button to opening the Navigation Drawer.
     */
    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (isDrawerOpen()) {
                closeDrawer();
            } else {
                openDrawer();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Search helper method.
     *
     * @param intent search intent
     */
    public void handleSearchQuery(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            onSearch(intent);
        }
    }

    /**
     * Handles and dispatch the search query intent received from the {@link SearchView}.
     *
     * @param intent search intent
     */
    private void onSearch(Intent intent) {
        final String query = intent.getStringExtra(SearchManager.QUERY);

        if (!AndroidUtils.isWifiConnected(BaseActivity.this) &&
                !AndroidUtils.isNetworkConnected(BaseActivity.this)) {
            if (mSearchView.requestFocus()) {
                mSearchView.setQuery(query, false);
            }
            return;
        }

        if (TextUtils.isDigitsOnly(query)) {
            final MaterialDialog dialog = ProgressDialogHelper.show(BaseActivity.this,
                    R.string.info_title_searching, R.string.info_content_please_wait);

            mSearchView.clearFocus();

            String url = Uri.parse(getString(R.string.url_validate_rmtc_horarios_viagem))
                    .buildUpon()
                    .appendQueryParameter(
                            getString(R.string.query_param_validate_rmtc_horarios_viagem), query)
                    .build().toString();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            dialog.dismiss();
                            try {
                                String status = response.getString(getString(
                                        R.string.json_attr_status_validate_rmtc_horarios_viagem));

                                if (getString(
                                        R.string.json_attr_success_validate_rmtc_horarios_viagem)
                                        .equals(status)) {
                                    SuggestionsProviderManager.getInstance(BaseActivity.this)
                                            .saveQuery(query, null);
                                    searchStopCode(query);
                                } else {
                                    SnackBarHelper.show(BaseActivity.this, response.getString(
                                            getString(R.string
                                                    .json_attr_message_validate_rmtc_horarios_viagem)));
                                }
                            } catch (JSONException e) {
                                Timber.e(String.format(
                                                getString(R.string.log_event_error_network_request),
                                                e.getClass().toString(), "onResponse", "JSONObject",
                                                query),
                                        e);
                                SnackBarHelper.show(BaseActivity.this,
                                        getString(R.string.error_in_network_search_request));
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismiss();
                            Timber.e(String.format(
                                    getString(R.string.log_event_error_network_request),
                                    error.getClass().toString(), "onErrorResponse", "JSONObject",
                                    query), error);
                            SnackBarHelper.show(BaseActivity.this,
                                    getString(R.string.error_in_network_search_request));
                        }
                    }
            );

            RequestQueueManager.getInstance(BaseActivity.this).addToRequestQueue(request, TAG);
        } else {
            SnackBarHelper.showSingleLine(BaseActivity.this, getString(
                    R.string.non_digit_voice_search));
        }
    }

    @Override
    public void onClick(final MaterialSection section) {
        if (section == horarioViagemSection) {
            HorarioViagemFragment fragment = (HorarioViagemFragment) section.getTargetFragment();

            if (fragment.isViewingBusStopPage()) {
                fragment.reloadPageFromArguments(buildFinalUrl(getString(
                        R.string.url_rmtc_horario_viagem)));
                return;
            } else {
                section.setTarget(newHorarioViagemPageFragment(BaseActivity.this));
            }
        }
        super.onClick(section);
    }

    /**
     * Performs search in the fragment {@link mx.x10.filipebezerra.horariosrmtcgoiania.fragments.HorarioViagemFragment}
     *
     * @param stopCode bus stop number retrieved from {@link SearchManager#QUERY}
     */
    @SuppressWarnings("unchecked")
    public void searchStopCode(final String stopCode) {
        HorarioViagemFragment fragment = (HorarioViagemFragment) horarioViagemSection
                .getTargetFragment();

        if (fragment.isViewingBusStopPage()) {
            Bundle arguments = buildHorarioViagemUrl(BaseActivity.this, stopCode);
            fragment.reloadPageFromArguments(arguments);
        } else {
            MaterialSection currentSection = getCurrentSection();

            if (horarioViagemSection != currentSection) {
                currentSection.unSelect();
                horarioViagemSection.select();
                changeToolbarColor(horarioViagemSection);
            }
            setFragment(WebViewFragmentFactory.newHorarioViagemPageFragment(
                    BaseActivity.this, stopCode), horarioViagemSection.getTitle());
            setSection(horarioViagemSection);
        }
    }
}