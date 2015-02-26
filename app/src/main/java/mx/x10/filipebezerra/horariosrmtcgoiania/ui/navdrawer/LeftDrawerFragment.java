package mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.Arrays;
import java.util.List;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.adapter.NavDrawerAdapter;
import mx.x10.filipebezerra.horariosrmtcgoiania.app.ApplicationSingleton;
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

        postDrawerItemSelectionEvent(new DrawerItemSelectionEvent(new DrawerItemSelectionMessage(
                getDrawerItemHorariosViagem(), null)));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);

        NavDrawerItem drawerItem = mListAdapter.getItem(position);
        postDrawerItemSelectionEvent(new DrawerItemSelectionEvent(new DrawerItemSelectionMessage(
                drawerItem, null)));
    }
    
    public static NavDrawerItem getDrawerItemHorariosViagem() {
        return mNavDrawerItemHorariosViagem;
    }

    private String getFavoriteCount() {
        return String.valueOf(ApplicationSingleton.getInstance().getDaoSession()
                .getFavoriteBusStopDao().count());
    }

    private List<NavDrawerItem> getDrawerItems() {
        mNavDrawerItemHorariosViagem = NavMenuItem.create(ID_DRAWER_MENU_ITEM_HORARIOS_VIAGEM,
                mActivity.getString(R.string.drawer_menu_item_rmtc_horarios_viagem),
                "ic_alarm_white_24dp", true, mActivity);
        
        return Arrays.asList(
                NavMenuSection.create(ID_DRAWER_MENU_SECTION_FAVORITES,
                        mActivity.getString(R.string.drawer_menu_section_favorites)),

                NavMenuItem.create(ID_DRAWER_MENU_ITEM_FAVORITE_BUS_STOPS,
                        mActivity.getString(R.string.drawer_menu_item_favorite_bus_stops),
                        "ic_favorite_white_24dp", getFavoriteCount(), true, false,
                        mActivity),

                NavMenuSection.create(ID_DRAWER_MENU_SECTION_SERVICOS_RMTC,
                        mActivity.getString(R.string.drawer_menu_section_rmtc_services)),

                mNavDrawerItemHorariosViagem,

                NavMenuItem.create(ID_DRAWER_MENU_ITEM_PLANEJE_VIAGEM,
                        mActivity.getString(R.string.drawer_menu_item_rmtc_planeje_viagem),
                        "ic_map_white_24dp", true, mActivity),

                NavMenuItem.create(ID_DRAWER_MENU_ITEM_PONTO_A_PONTO,
                        mActivity.getString(R.string.drawer_menu_item_rmtc_ponto_a_ponto),
                        "ic_place_white_24dp", true, mActivity),

                NavMenuItem.create(ID_DRAWER_MENU_MENU_ITEM_SAC,
                        mActivity.getString(R.string.drawer_menu_item_rmtc_sac),
                        "ic_call_white_24dp", true, mActivity),

                NavMenuSection.create(ID_DRAWER_MENU_SECTION_ABOUT,
                        mActivity.getString(R.string.drawer_menu_section_about)),

                NavMenuItem.create(ID_DRAWER_FIXED_MENU_ITEM_HELP,
                        mActivity.getString(R.string.drawer_fixed_menu_item_help),
                        "ic_help_white_24dp", false, mActivity),

                NavMenuItem.create(ID_DRAWER_FIXED_MENU_ITEM_CONFIGURATIONS,
                        mActivity.getString(R.string.drawer_fixed_menu_item_configurations),
                        "ic_settings_white_24dp", false, mActivity)
        );
    }
    
}