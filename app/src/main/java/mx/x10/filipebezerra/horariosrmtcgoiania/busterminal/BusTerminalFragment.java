package mx.x10.filipebezerra.horariosrmtcgoiania.busterminal;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Bind;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.base.BaseFragment;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 03/01/2016
 * @since 3.0.0
 */
public class BusTerminalFragment extends BaseFragment {

    @Bind(R.id.list) protected RecyclerView mBusTerminalsView;

    private BusTerminalAdapter mBusTerminalAdapter;

    @Override
    protected int provideLayoutResource() {
        return R.layout.fragment_bus_terminal;
    }

    public static BusTerminalFragment newInstance() {
        return new BusTerminalFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBusTerminalsView.setHasFixedSize(true);
        mBusTerminalsView.setAdapter(
                mBusTerminalAdapter = new BusTerminalAdapter(getActivity()));
    }
}
