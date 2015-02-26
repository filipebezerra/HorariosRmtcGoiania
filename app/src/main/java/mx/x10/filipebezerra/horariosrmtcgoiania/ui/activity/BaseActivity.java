package mx.x10.filipebezerra.horariosrmtcgoiania.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.squareup.otto.Bus;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.EventBusProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.NetworkUtils;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.SnackBarHelper;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected Toolbar mToolbar;
    protected Bus mEventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        
        mEventBus = EventBusProvider.getInstance().getEventBus();
        mEventBus.register(this);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true); // TODO checar comportamento, compatibilidade com toolbar
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    protected abstract int getLayoutResource();

    protected void setActionBarIcon(int iconRes) {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(iconRes);
        }
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
