package mx.x10.filipebezerra.horariosrmtcgoiania.busterminal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.view.ViewGroup;
import butterknife.Bind;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.base.BaseActivity;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 03/01/2016
 * @since 3.0.0
 */
public class BusTerminalActivity extends BaseActivity {

    @Bind(R.id.root_layout) protected CoordinatorLayout mRootLayout;

    @Override
    protected int provideLayoutResource() {
        return R.layout.activity_bus_terminal;
    }

    @NonNull
    @Override
    public ViewGroup getRootViewLayout() {
        return mRootLayout;
    }

    @Override
    protected void onCreate(Bundle inState) {
        super.onCreate(inState);

        if (inState == null) {
            replaceFragment(BusTerminalFragment.newInstance(), false);
        }
    }
}
