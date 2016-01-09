package mx.x10.filipebezerra.horariosrmtcgoiania.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.otto.Bus;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.activities.BaseActivity;
import mx.x10.filipebezerra.horariosrmtcgoiania.managers.DaoManager;
import mx.x10.filipebezerra.horariosrmtcgoiania.managers.RequestQueueManager;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.FavoriteBusStop;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.dao.FavoriteBusStopDao;
import mx.x10.filipebezerra.horariosrmtcgoiania.network.parsers.BusStopHtmlParser;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.events.EventBusProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.events.NotificationEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.events.NotificationMessage;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.helpers.ProgressDialogHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.views.helpers.SnackBarHelper;
import net.i2p.android.ext.floatingactionbutton.FloatingActionButton;
import net.i2p.android.ext.floatingactionbutton.FloatingActionsMenu;
import timber.log.Timber;

/**
 * Fragment composed by a {@link android.webkit.WebView}, an animated
 * {@link com.afollestad.materialdialogs.MaterialDialog} and a special action
 * {@link net.i2p.android.ext.floatingactionbutton.FloatingActionButton} based in Material design.
 *
 * @author Filipe Bezerra
 * @version 2.3, 09/01/2016
 * @since 1.6
 * @see mx.x10.filipebezerra.horariosrmtcgoiania.fragments.BaseWebViewFragment
 */
public class HorarioViagemFragment extends BaseWebViewFragment {
    private static final String TAG = HorarioViagemFragment.class.getSimpleName();

    private boolean mIsViewingBusStopPage = false;

    private FavoriteBusStopDao mFavoriteBusStopDao;

    /**
     * If {@link #mIsViewingBusStopPage} then in {@link #onWebViewPageFinished} will try to retrieve
     * the data associated with the bus stop code calling {@link #getArgBusStopCode} if was explicitly
     * passed into {@link android.support.v4.app.Fragment#setArguments(android.os.Bundle)} or
     * calling {@link #getBusStopCodeFromCurrentUrl} if was a search using the webview fields.
     */
    private FavoriteBusStop mPersistedFavoriteBusStop;

    @NonNull private Bus mEventBus;

    @Bind(R.id.fab_bookmark_stop_bus) FloatingActionButton mFabBookmarkStopBus;

    public static final String ARG_PARAM_BUS_STOP_CODE = BaseWebViewFragment.class.getSimpleName()
            + ".ARG_PARAM_BUS_STOP_CODE";

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        if (getArgBusStopCode() != null) {
            mIsViewingBusStopPage = true;
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        return setupContentView(inflater.inflate(R.layout.fragment_horario_viagem, container, false));
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFavoriteBusStopDao = DaoManager.getInstance(getActivity()).getFavoriteBusStopDao();
        mEventBus = EventBusProvider.getInstance().getEventBus();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.menu_update_bus_travel_times, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_refresh:
                setSwipeRefreshing(true);
                initiateReloading();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public View setupContentView(final View fragmentView) {
        super.setupContentView(fragmentView);
        return fragmentView;
    }

    private void invalidateViews() {
        if (mFabBookmarkStopBus != null) {
            mFabBookmarkStopBus.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onWebViewOfflinePageLoaded() {
        super.onWebViewOfflinePageLoaded();
        invalidateViews();
    }

    @Override
    public void onWebViewPageStarted() {
        super.onWebViewPageStarted();
        invalidateViews();
    }

    @Override
    public void onWebViewPageFinished() {
        super.onWebViewPageFinished();

        if (mFabBookmarkStopBus != null) {
            if (!mIsViewingBusStopPage && getBusStopCodeFromCurrentUrl() != null) {
                mIsViewingBusStopPage = true;
            }

            if (mIsViewingBusStopPage) {
                FloatingActionsMenu fabMenu = ((BaseActivity) getActivity()).getFabMenu();
                if (fabMenu != null) {
                    fabMenu.setVisibility(View.GONE);
                }
                mFabBookmarkStopBus.setVisibility(View.VISIBLE);

                mPersistedFavoriteBusStop = getPersistedFavoriteBusStop();
                Timber.d(String.format(getString(R.string.log_event_debug),
                        "onWebViewPageFinished", "persisted favorite bus stop",
                        mPersistedFavoriteBusStop == null ? "but it\'s not" :
                                "is persisted with id: "+mPersistedFavoriteBusStop.getId()));

                if (mPersistedFavoriteBusStop != null) {
                    mFabBookmarkStopBus.setIconDrawable(ContextCompat.getDrawable(getActivity(),
                            R.drawable.ic_favorite));
                }
            }
        }
    }

    @OnClick(R.id.fab_bookmark_stop_bus)
    protected void updateFavoriteStopBus() {
        if (mIsViewingBusStopPage) {
            if (mPersistedFavoriteBusStop == null) {
                final String currentUrl = mWebView.getUrl();

                final MaterialDialog dialog = ProgressDialogHelper.show(getActivity(),
                        R.string.info_title_adding,
                        R.string.info_content_please_wait);

                StringRequest request = new StringRequest(currentUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String result) {
                                FavoriteBusStop newFavoriteBusStop = new BusStopHtmlParser()
                                        .parse(result);

                                mFavoriteBusStopDao.insert(newFavoriteBusStop);
                                mPersistedFavoriteBusStop = newFavoriteBusStop;

                                mEventBus.post(new NotificationEvent(new NotificationMessage(
                                        NotificationMessage.NotificationType.INCREMENT)));

                                dialog.dismiss();

                                SnackBarHelper.showSingleLine(getActivity(),
                                        getString(R.string.info_stop_bus_added_to_favorites),
                                        mFabBookmarkStopBus);
                                mFabBookmarkStopBus.setIconDrawable(
                                        ContextCompat.getDrawable(getActivity(),
                                                R.drawable.ic_favorite));
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                dialog.dismiss();
                                Timber.e(TAG, String.format(
                                        getString(R.string.log_event_error_network_request),
                                        error.getClass().toString(), "onErrorResponse",
                                        "String",
                                        currentUrl), error);
                                SnackBarHelper.show(getActivity(), getString(
                                        R.string.error_in_network_search_request_parsing_html_page),
                                        mFabBookmarkStopBus);
                            }
                        }
                );
                RequestQueueManager.getInstance(getActivity()).addToRequestQueue(request,
                        TAG);
            } else {
                final MaterialDialog dialog = ProgressDialogHelper.show(getActivity(),
                        R.string.info_title_removing,
                        R.string.info_content_please_wait);

                mFavoriteBusStopDao.delete(mPersistedFavoriteBusStop);
                mPersistedFavoriteBusStop = null;

                mEventBus.post(new NotificationEvent(new NotificationMessage(
                        NotificationMessage.NotificationType.DECREMENT)));

                dialog.dismiss();

                SnackBarHelper.showSingleLine(getActivity(),
                        getString(R.string.info_stop_bus_removed_from_favorites),
                        mFabBookmarkStopBus);
                mFabBookmarkStopBus.setIconDrawable(ContextCompat.getDrawable(getActivity(),
                        R.drawable.ic_unmark_favorite));
            }
        }
    }

    protected FavoriteBusStop getPersistedFavoriteBusStop() {
        if (mIsViewingBusStopPage) {
            Integer busStopCode = getArgBusStopCode();

            if (busStopCode == null) {
                busStopCode = getBusStopCodeFromCurrentUrl();
            }

            return mFavoriteBusStopDao.queryBuilder()
                    .where(FavoriteBusStopDao.Properties.StopCode.eq(busStopCode))
                    .unique();
        } else
            return null;
    }

    /**
     * Returns the bus stop code passed to arguments in the helper constructor.
     *
     * @return bus stop code passed to arguments
     */
    protected Integer getArgBusStopCode() {
        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_PARAM_BUS_STOP_CODE)) {
                try {
                    return Integer.parseInt(getArguments().getString(ARG_PARAM_BUS_STOP_CODE));
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }

    protected Integer getBusStopCodeFromCurrentUrl() {
        try {
            final String lastPathSegment = Uri.parse(getWebView().getUrl()).getLastPathSegment();
            return Integer.parseInt(lastPathSegment);
        } catch (NullPointerException | NumberFormatException e) {
            return null;
        }
    }

    public void reloadPageFromArguments(@Nullable final Bundle newPageArgs) {
        WebView webView = getWebView();

        if (webView == null)
            return;

        if (newPageArgs == null) {
            webView.loadUrl(getArguments().getString(ARG_PARAM_URL_PAGE));
        } else {
            webView.loadUrl(newPageArgs.getString(ARG_PARAM_URL_PAGE));
        }
    }

    public boolean isViewingBusStopPage() {
        return mIsViewingBusStopPage;
    }
}