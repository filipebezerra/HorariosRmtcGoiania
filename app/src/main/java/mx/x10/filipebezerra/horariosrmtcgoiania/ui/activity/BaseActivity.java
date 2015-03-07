package mx.x10.filipebezerra.horariosrmtcgoiania.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.app.ApplicationSingleton;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.HorarioViagemFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.PlanejeViagemFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.PontoToPontoFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.SacFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.WapFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.NetworkUtils;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.SnackBarHelper;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public abstract class BaseActivity extends MaterialNavigationDrawer {

    @Override
    public void init(final Bundle savedInstanceState) {
        MaterialAccount account = new MaterialAccount(getResources(), 
                "Filipe Bezerra", "filipebzerra@gmail.com",R.drawable.photo, R.drawable.bamboo);
        addAccount(account);

        addSection(newSection(getString(R.string.navdrawer_menu_item_favorite_bus_stops), 
                R.drawable.ic_drawer_pontos_favoritos, new WapFragment())
                .setNotifications(getFavoriteCount())
                .setSectionColor(Color.parseColor("#FF5722"), Color.parseColor("#E64A19")));
        addSection(newSection(getString(R.string.navdrawer_menu_item_rmtc_wap), 
                R.drawable.ic_drawer_wap, new WapFragment())
                .setSectionColor(Color.parseColor("#9C27B0"), Color.parseColor("#7B1FA2")));
        addSection(newSection(getString(R.string.navdrawer_menu_item_rmtc_horarios_viagem), 
                R.drawable.ic_drawer_horario_viagem, new HorarioViagemFragment())
                .setSectionColor(Color.parseColor("#009688"), Color.parseColor("#00796B")));
        addSection(newSection(getString(R.string.navdrawer_menu_item_rmtc_planeje_viagem), 
                R.drawable.ic_drawer_planeje_sua_viagem, new PlanejeViagemFragment())
                .setSectionColor(Color.parseColor("#E91E63"), Color.parseColor("#C2185B")));
        addSection(newSection(getString(R.string.navdrawer_menu_item_rmtc_ponto_a_ponto), 
                R.drawable.ic_drawer_ponto_a_ponto, new PontoToPontoFragment())
                .setSectionColor(Color.parseColor("#3F51B5"), Color.parseColor("#303F9F")));
        addSection(newSection(getString(R.string.navdrawer_menu_item_rmtc_sac), 
                R.drawable.ic_drawer_sac, new SacFragment())
                .setSectionColor(Color.parseColor("#4CAF50"), Color.parseColor("#388E3C")));
        
        addBottomSection(newSection(getString(R.string.navdrawer_fixed_menu_item_help), 
                R.drawable.ic_drawer_help, new SacFragment())
                .setSectionColor(Color.parseColor("#FFC107"), Color.parseColor("#FFA000")));
        addBottomSection(newSection(getString(R.string.navdrawer_fixed_menu_item_configurations), 
                R.drawable.ic_drawer_settings, new Intent(this, SettingsActivity.class))
                .setSectionColor(Color.parseColor("#795548"), Color.parseColor("#5D4037")));
    }

    private int getFavoriteCount() {
        return (int) ApplicationSingleton.getInstance().getDaoSession()
                .getFavoriteBusStopDao().count();
    }

    // TODO : register for bus events
    /*
    @Override
    protected void onStart() {
        super.onStart();
        EventBusProvider.getInstance().getEventBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBusProvider.getInstance().getEventBus().unregister(this);
    }
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

    protected BroadcastReceiver mConnectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        if (!NetworkUtils.isConnectingToInternet(context)) {
            SnackBarHelper.show(BaseActivity.this, getString(R.string.no_internet_connectivity));
        }
        }
    };
}
