package mx.x10.filipebezerra.horariosrmtcgoiania.activities;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.util.Utils;
import java.util.ArrayList;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.fragments.FavoriteBusStopListFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.fragments.HorarioViagemFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.fragments.WebViewFragmentFactory;
import mx.x10.filipebezerra.horariosrmtcgoiania.managers.DaoManager;
import mx.x10.filipebezerra.horariosrmtcgoiania.managers.RequestQueueManager;
import mx.x10.filipebezerra.horariosrmtcgoiania.managers.SuggestionsProviderManager;
import mx.x10.filipebezerra.horariosrmtcgoiania.utils.AndroidUtils;
import mx.x10.filipebezerra.horariosrmtcgoiania.utils.PrefUtils;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.events.EventBusProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.helpers.ProgressDialogHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.helpers.SnackBarHelper;
import net.i2p.android.ext.floatingactionbutton.FloatingActionButton;
import net.i2p.android.ext.floatingactionbutton.FloatingActionsMenu;
import org.json.JSONException;
import org.json.JSONObject;
import timber.log.Timber;

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
 * @version 2.3, 09/01/2016
 * @since #
 */
public abstract class BaseActivity extends MaterialNavigationDrawer {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final int REQUEST_CODE_SPEECH_INPUT = 100;
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
            if (AndroidUtils.checkAndNotifyNetworkState(context, mFabMenu)) {
                Timber.d(String.format(
                        getString(R.string.log_event_debug), "onReceive",
                        intent.getAction(), "no internet connectivity"));
            }
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

    public FloatingActionsMenu getFabMenu() {
        return mFabMenu;
    }

    @Bind(R.id.fab_menu) protected FloatingActionsMenu mFabMenu;
    @Bind(R.id.fab_search_stop_bus) protected FloatingActionButton mFabVoiceSearch;
    @Bind(R.id.fab_share_app) protected FloatingActionButton mFabShareApp;
    @Bind(R.id.fab_evaluate_app) protected FloatingActionButton mFabEvaluateApp;

    private int mLastOrientationConfiguration;

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

        initDrawerLearningPattern();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Timber.tag(TAG);

        addFabMenuView();

        int initialOrientation = getWindowManager().getDefaultDisplay().getRotation();
        if (initialOrientation == Surface.ROTATION_0 || initialOrientation == Surface.ROTATION_180)
            mLastOrientationConfiguration = Configuration.ORIENTATION_PORTRAIT;
        else
            mLastOrientationConfiguration = Configuration.ORIENTATION_LANDSCAPE;

        EventBusProvider.getInstance().getEventBus().register(BaseActivity.this);
    }

    private void initSpeechInputSearchAction() {
        final Intent speechInputIntent = AndroidUtils.createSpeechInputIntent(BaseActivity.this);
        if (speechInputIntent == null) {
            mFabMenu.removeButton(mFabVoiceSearch);
            return;
        }

        mFabVoiceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AndroidUtils.checkAndNotifyNetworkState(BaseActivity.this, mFabMenu))
                    return;

                mFabMenu.collapse();
                startActivityForResult(speechInputIntent, REQUEST_CODE_SPEECH_INPUT);
            }
        });
    }

    private void initRatingAction() {
        mFabEvaluateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AndroidUtils.checkAndNotifyNetworkState(BaseActivity.this, mFabMenu))
                    return;

                mFabMenu.collapse();
                AndroidUtils.openAppRating(BaseActivity.this);
            }
        });
    }

    private void addFabMenuView() {
        getLayoutInflater().inflate(R.layout.view_floating_action_button,
                (android.view.ViewGroup) findViewById(
                        it.neokree.materialnavigationdrawer.R.id.content), true);

            ButterKnife.bind(BaseActivity.this);

        initSpeechInputSearchAction();
        initShareAction();
        initRatingAction();
    }

    private void initDrawerLearningPattern() {
        if (PrefUtils.isWelcomeDone(BaseActivity.this)) {
            disableLearningPattern();
        } else {
            PrefUtils.markWelcomeDone(BaseActivity.this);
        }
    }

    private void initShareAction() {
        final Intent shareIntent = AndroidUtils.createShareIntent(BaseActivity.this);
        if (shareIntent == null) {
            mFabMenu.removeButton(mFabShareApp);
            return;
        }

        mFabShareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AndroidUtils.checkAndNotifyNetworkState(BaseActivity.this, mFabMenu))
                    return;

                mFabMenu.collapse();
                startActivity(Intent.createChooser(shareIntent,
                        getString(R.string.share_dialog_title)));
            }
        });
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
        favoriteBusStopSection = newSection(
                getString(R.string.navdrawer_section_favorite_bus_stops),
                R.drawable.ic_drawer_pontos_favoritos, new FavoriteBusStopListFragment());

        final int favoriteCount = (int) DaoManager.getInstance(BaseActivity.this)
                .getFavoriteBusStopDao().count();

        if (favoriteCount != 0) {
            favoriteBusStopSection.setNotifications(favoriteCount);
        }

        addSection(favoriteBusStopSection
                .setSectionColor(
                        ContextCompat.getColor(this,
                                R.color.navdrawer_favorites_section_color),
                        ContextCompat.getColor(this,
                                R.color.navdrawer_favorites_section_dark_color)));
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
                        ContextCompat.getColor(this,
                                R.color.navdrawer_wap_section_color),
                        ContextCompat.getColor(this,
                                R.color.navdrawer_wap_section_dark_color)));
        addSection(horarioViagemSection = newSection(
                getString(R.string.navdrawer_section_rmtc_horario_viagem),
                R.drawable.ic_drawer_horario_viagem,
                newHorarioViagemPageFragment(BaseActivity.this))
                .setSectionColor(
                        ContextCompat.getColor(this,
                                R.color.navdrawer_horario_viagem_section_color),
                        ContextCompat.getColor(this,
                                R.color.navdrawer_horario_viagem_section_dark_color)));

        addDivisor();

        addSection(newSection(getString(R.string.navdrawer_section_rmtc_planeje_viagem),
                R.drawable.ic_drawer_planeje_sua_viagem,
                newPlanejeViagemPageFragment(BaseActivity.this))
                .setSectionColor(
                        ContextCompat.getColor(this,
                                R.color.navdrawer_planeje_sua_viagem_section_color),
                        ContextCompat.getColor(this,
                                R.color.navdrawer_planeje_sua_viagem_section_dark_color)));
        addSection(newSection(getString(R.string.navdrawer_section_rmtc_ponto_a_ponto),
                R.drawable.ic_drawer_ponto_a_ponto,
                newPontoaPontoPageFragment(BaseActivity.this))
                .setSectionColor(
                        ContextCompat.getColor(this,
                                R.color.navdrawer_ponto_a_ponto_section_color),
                        ContextCompat.getColor(this,
                                R.color.navdrawer_ponto_a_ponto_section_dark_color)));
        addSection(newSection(getString(R.string.navdrawer_section_rmtc_sac),
                R.drawable.ic_drawer_sac,
                newSacPageFragment(BaseActivity.this))
                .setSectionColor(
                        ContextCompat.getColor(this,
                                R.color.navdrawer_sac_section_color),
                        ContextCompat.getColor(this,
                                R.color.navdrawer_sac_section_dark_color)));
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

        // TODO : checkAndNotifyNetworkState e
        // if (sendIntent.resolveActivity(getPackageManager()) != null) {
        //    startActivity(chooser);
        //}

        addBottomSection(newSection(getString(R.string.navdrawer_bottom_section_configurations),
                R.drawable.ic_drawer_settings,
                new Intent(BaseActivity.this, SettingsActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mConnectionReceiver, new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mConnectionReceiver);
    }

    @Override
    protected void onDestroy() {
        EventBusProvider.getInstance().getEventBus().unregister(BaseActivity.this);
        super.onDestroy();
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
     * Handles search hardware button, for compatibility for old Android devices and
     * handles the menu hardware button to opening the Navigation Drawer.
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Timber.d("dispatchKeyEvent with event " + event);

        if (event.getAction() == KeyEvent.ACTION_UP) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_SEARCH:
                    if (MenuItemCompat.expandActionView(mSearchMenuItem)) {
                        mSearchView.requestFocus();
                        return true;
                    }
                case KeyEvent.KEYCODE_MENU:
                    // TODO: workaround of the bug closing drawer onClick and KEY_MENU pressed when is a tablet device
                    if (! deviceSupportMultiPane()) {
                        if (isDrawerOpen()) {
                            closeDrawer();
                        } else {
                            openDrawer();
                        }
                        return true;
                    }
                default:
                    return super.dispatchKeyEvent(event);
            }
        }

        return super.dispatchKeyEvent(event);
    }

    // TODO: Duplicated validation manually fixing bug https://github.com/neokree/MaterialNavigationDrawer/issues/263
    /**
     * Checks if the current orientation is {@link Configuration#ORIENTATION_LANDSCAPE} and the
     * current device is a tablet. This method suppose that <code>multipaneSupport</code> for
     * the current theme is <code>true</code>.
     * @return
     */
    private boolean deviceSupportMultiPane() {
        final Resources resources = getResources();
        return resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                Utils.isTablet(resources);
    }

    /**
     * Handles orientation configuration changes to layout floating actions correctly.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation != mLastOrientationConfiguration) {
            View fabMenuView = findViewById(R.id.fab_menu);
            if (fabMenuView != null) {
                ViewGroup parent = (ViewGroup) fabMenuView.getParent();

                if (parent != null) {
                    parent.removeView(fabMenuView);
                    addFabMenuView();
                }
            }

            mLastOrientationConfiguration = newConfig.orientation;
        }
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
        // TODO: workaround of the bug closing drawer onClick and KEY_MENU pressed when is a tablet device
        if (getCurrentSection().equals(section)) {
            if (! deviceSupportMultiPane()) {
                closeDrawer();
            }
            return;
        }

        if (section == horarioViagemSection) {
            HorarioViagemFragment fragment = (HorarioViagemFragment) section.getTargetFragment();

            if (fragment.isViewingBusStopPage()) {
                fragment.reloadPageFromArguments(buildFinalUrl(getString(
                        R.string.url_rmtc_horario_viagem)));
                return;
            } else {
                section.setTarget(newHorarioViagemPageFragment(BaseActivity.this));
            }
        } else {
            showFabMenu();
        }
        super.onClick(section);
    }

    public void showFabMenu() {
        if (mFabMenu != null) {
            mFabMenu.setVisibility(View.VISIBLE);
            if (mFabMenu.isExpanded())
                mFabMenu.collapse();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Timber.d("onActivityResult with request code "+requestCode+" with result code "+resultCode);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SPEECH_INPUT:
                    if (data != null) {
                        Timber.d("Request REQUEST_CODE_SPEECH_INPUT returned with data %s. "
                                + "Only first will be used", data);
                        ArrayList<String> extras = data.getStringArrayListExtra(
                                RecognizerIntent.EXTRA_RESULTS);
                        Intent searchIntent = new Intent(Intent.ACTION_SEARCH);
                        searchIntent.putExtra(SearchManager.QUERY, extras.get(0));
                        onSearch(searchIntent);
                    } else {
                        Timber.d("No data returned with request REQUEST_CODE_SPEECH_INPUT");
                    }
                    break;
            }
        }
    }

    /**
     * Performs search in the fragment {@link mx.x10.filipebezerra.horariosrmtcgoiania.fragments.HorarioViagemFragment}
     *
     * @param stopCode bus stop number retrieved from {@link SearchManager#QUERY}
     */
    @SuppressWarnings("unchecked")
    public void searchStopCode(final String stopCode) {
        mFabMenu.setVisibility(View.GONE);
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