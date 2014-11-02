package mx.x10.filipebezerra.horariosrmtcgoiania.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.NetworkUtils;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.ToastHelper;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public abstract class BaseActivity extends ActionBarActivity {

    public Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
        if (NetworkUtils.isConnectingToInternet(context) == false) {
            new ToastHelper(BaseActivity.this).showNoConnectionAlert();
        }
        }
    };
}
