package mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;

import com.squareup.otto.Bus;

import mx.x10.filipebezerra.horariosrmtcgoiania.event.DrawerItemSelectionEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.EventBusProvider;

/**
 * @author Filipe Bezerra
 * @version 2.0
 * @since 2.0_23/02/2015
 */
public abstract class BaseDrawerSideFragment extends ListFragment
        implements AdapterView.OnItemClickListener{

    protected Bus mEventBus;

    protected Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEventBus = EventBusProvider.getInstance().getEventBus();
        mEventBus.register(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        getListView().setItemChecked(position, true);
        getListView().setSelection(position);
    }

    public void postDrawerItemSelectionEvent(final DrawerItemSelectionEvent event) {
        mEventBus.post(event);
    }

}
