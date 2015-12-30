package mx.x10.filipebezerra.horariosrmtcgoiania.arrivalprediction;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.base.BaseFragment;
import org.parceler.Parcels;

/**
 * Arrival prediction visualization.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 29/12/2015
 * @since 0.1.0
 */
public class ArrivalPredictionFragment extends BaseFragment {

    private static final String ARG_DATA = "ArrivalPrediction";

    private ArrivalPrediction mArrivalPrediction;

    @Override
    protected int provideLayoutResource() {
        return R.layout.fragment_arrival_prediction;
    }

    public static ArrivalPredictionFragment newInstance(@NonNull ArrivalPrediction data) {
        ArrivalPredictionFragment fragment = new ArrivalPredictionFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_DATA, Parcels.wrap(data));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mArrivalPrediction = Parcels.unwrap(getArguments().getParcelable(ARG_DATA));
    }
}
