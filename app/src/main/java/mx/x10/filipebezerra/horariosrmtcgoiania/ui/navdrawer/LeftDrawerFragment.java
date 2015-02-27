package mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.adapter.NavDrawerAdapter;
import mx.x10.filipebezerra.horariosrmtcgoiania.app.ApplicationSingleton;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.CounterNotificationEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.CounterNotificationMessage;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.DrawerItemSelectionEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.DrawerItemSelectionMessage;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.NavDrawerItem;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.NavMenuItem;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.NavMenuSection;

/**
 * Left drawer fragment composed for the static menus.
 *
 * @author Filipe Bezerra
 * @version 2.0
 * @since 2.0_23/02/2015
 */
public class LeftDrawerFragment extends BaseDrawerSideFragment {

    private static final String LOG_TAG = LeftDrawerFragment.class.getSimpleName();
    
    public static final int ID_DRAWER_MENU_SECTION_FAVORITES = 10;
    public static final int ID_DRAWER_MENU_ITEM_FAVORITE_BUS_STOPS = 11;
    public static final int ID_DRAWER_MENU_SECTION_SERVICOS_RMTC = 20;
    public static final int ID_DRAWER_MENU_ITEM_HORARIOS_VIAGEM = 21;
    public static final int ID_DRAWER_MENU_ITEM_PLANEJE_VIAGEM = 22;
    public static final int ID_DRAWER_MENU_ITEM_PONTO_A_PONTO = 23;
    public static final int ID_DRAWER_MENU_MENU_ITEM_SAC = 24;
    public static final int ID_DRAWER_MENU_SECTION_ABOUT = 30;
    public static final int ID_DRAWER_FIXED_MENU_ITEM_HELP = 33;
    public static final int ID_DRAWER_FIXED_MENU_ITEM_CONFIGURATIONS = 34;
    
    private static NavDrawerItem mNavDrawerItemHorariosViagem = null;

    private NavDrawerAdapter mListAdapter;

    /**
     * A counter buffer to store each View that receives notifications to update their
     * counter.
     */
    private Map<Integer, Long> mBufferCounterView = new HashMap<>();

    public static NavDrawerItem getDrawerItemHorariosViagem() {
        return mNavDrawerItemHorariosViagem;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_left_drawer, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListAdapter = new NavDrawerAdapter(mActivity, R.layout.navdrawer_left_items,
                getDrawerItems());
        setListAdapter(mListAdapter);
        getListView().setOnItemClickListener(LeftDrawerFragment.this);

        int position = mListAdapter.getPosition(getDrawerItemHorariosViagem());
        onItemClick(getListView(), null, position, position);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);

        NavDrawerItem drawerItem = mListAdapter.getItem(position);
        postDrawerItemSelectionEvent(new DrawerItemSelectionEvent(new DrawerItemSelectionMessage(
                drawerItem, null)));
    }

    @Subscribe
    public void onCounterNotificationEvent(CounterNotificationEvent event) {
        final CounterNotificationMessage.NotificationType notificationType = event.getMessage()
                .getmNotificationType();
        final int viewId = event.getMessage().getmViewId();

        View view = getView().findViewById(viewId);

        if (view == null)
            return;

        if (!(view instanceof TextView))
            return;

        TextView textView = (TextView) view;

        switch (notificationType) {
            case START:
                textView.setText("1");
                removeBufferCounterView(viewId);
                break;

            case INCREMENT:
                CharSequence strCounter = textView.getText();

                if (TextUtils.isEmpty(strCounter)) {
                    textView.setText("1");
                    removeBufferCounterView(viewId);
                } else {
                    if ("99+".equals(strCounter.toString())) {
                        incrementBufferCounterView(viewId);
                        return;
                    }

                    int intCounter;
                    try {
                        intCounter = Integer.parseInt(strCounter.toString());
                    } catch (NumberFormatException e) {
                        intCounter = 0;
                    }

                    if (intCounter == 99) {
                        textView.setText("99+");
                        addBufferCounterView(viewId);
                    } else {
                        ++intCounter;
                        textView.setText(String.valueOf(intCounter));
                    }
                }
                break;

            case DECREMENT:
                strCounter = textView.getText();

                if (TextUtils.isEmpty(strCounter)) {
                    textView.setText("0");
                    removeBufferCounterView(viewId);
                } else {
                    if ("99+".equals(strCounter.toString())) {
                        Long counterValue = getBufferCounterView(viewId);

                        if (counterValue == 100) {
                            textView.setText("99");
                            removeBufferCounterView(viewId);
                        } else {
                            decrementBufferCounterView(viewId);
                        }
                        return;
                    }
                }
                break;

            case RESET:
                textView.setText("0");
                removeBufferCounterView(viewId);
                break;
        }
    }

    private void addBufferCounterView(int viewId) {
        mBufferCounterView.put(viewId, 100L);
    }

    private Long getBufferCounterView(int viewId) {
        return mBufferCounterView.get(viewId);
    }

    private void decrementBufferCounterView(int viewId) {
        if (! mBufferCounterView.containsKey(viewId))
            return;

        mBufferCounterView.put(viewId, mBufferCounterView.get(viewId) - 1);
    }

    private void incrementBufferCounterView(int viewId) {
        if (! mBufferCounterView.containsKey(viewId))
            return;

        mBufferCounterView.put(viewId, mBufferCounterView.get(viewId) + 1);
    }

    private void removeBufferCounterView(int viewId) {
        if (mBufferCounterView.containsKey(viewId))
            mBufferCounterView.remove(viewId);
    }

    private String getFavoriteCount() {
        long dataCount = ApplicationSingleton.getInstance().getDaoSession()
                .getFavoriteBusStopDao().count();
        
        if (dataCount > 99) {
            return "99+";
        } else {
            return String.valueOf(dataCount);
        }
    }

    private List<NavDrawerItem> getDrawerItems() {
        mNavDrawerItemHorariosViagem = NavMenuItem.create(ID_DRAWER_MENU_ITEM_HORARIOS_VIAGEM,
                mActivity.getString(R.string.navdrawer_menu_item_rmtc_horarios_viagem),
                "ic_drawer_horario_viagem", true, mActivity);

        return Arrays.asList(
                NavMenuSection.create(ID_DRAWER_MENU_SECTION_FAVORITES,
                        mActivity.getString(R.string.navdrawer_menu_section_favorites)),

                NavMenuItem.create(ID_DRAWER_MENU_ITEM_FAVORITE_BUS_STOPS,
                        mActivity.getString(R.string.navdrawer_menu_item_favorite_bus_stops),
                        "ic_drawer_pontos_favoritos", getFavoriteCount(), true, false,
                        mActivity),

                NavMenuSection.create(ID_DRAWER_MENU_SECTION_SERVICOS_RMTC,
                        mActivity.getString(R.string.navdrawer_menu_section_rmtc_services)),

                mNavDrawerItemHorariosViagem,

                NavMenuItem.create(ID_DRAWER_MENU_ITEM_PLANEJE_VIAGEM,
                        mActivity.getString(R.string.navdrawer_menu_item_rmtc_planeje_viagem),
                        "ic_drawer_planeje_sua_viagem", true, mActivity),

                NavMenuItem.create(ID_DRAWER_MENU_ITEM_PONTO_A_PONTO,
                        mActivity.getString(R.string.navdrawer_menu_item_rmtc_ponto_a_ponto),
                        "ic_drawer_ponto_a_ponto", true, mActivity),

                NavMenuItem.create(ID_DRAWER_MENU_MENU_ITEM_SAC,
                        mActivity.getString(R.string.navdrawer_menu_item_rmtc_sac),
                        "ic_drawer_sac", true, mActivity),

                NavMenuSection.create(ID_DRAWER_MENU_SECTION_ABOUT,
                        mActivity.getString(R.string.navdrawer_menu_section_about)),

                NavMenuItem.create(ID_DRAWER_FIXED_MENU_ITEM_HELP,
                        mActivity.getString(R.string.navdrawer_fixed_menu_item_help),
                        "ic_drawer_help", false, mActivity),

                NavMenuItem.create(ID_DRAWER_FIXED_MENU_ITEM_CONFIGURATIONS,
                        mActivity.getString(R.string.navdrawer_fixed_menu_item_configurations),
                        "ic_drawer_settings", false, mActivity)
        );
    }

}