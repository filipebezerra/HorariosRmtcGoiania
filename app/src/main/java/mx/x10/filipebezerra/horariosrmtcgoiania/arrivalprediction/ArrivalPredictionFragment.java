package mx.x10.filipebezerra.horariosrmtcgoiania.arrivalprediction;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.base.BaseFragment;
import org.parceler.Parcels;
import timber.log.Timber;

/**
 * Arrival prediction visualization.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 29/12/2015
 * @since 0.1.0
 */
public class ArrivalPredictionFragment extends BaseFragment {

    private static final String ARG_DATA = "ArrivalPrediction";
    private static final String LOG = ArrivalPredictionFragment.class.getSimpleName();

    private ArrivalPrediction mArrivalPrediction;

    @Bind(R.id.next_arrives_in_minutes) TextView mNextArrivesMinutesView;
    @Bind(R.id.next_travel_quality) TextView mNextTravelQualityView;
    @Bind(R.id.next_bus_number) TextView mNextBusNumber;
    @Bind(R.id.next_expected_arrival) TextView mNextExpectedArrival;
    @Bind(R.id.next_planned_arrival) TextView mNextPlannedArrival;

    @Bind(R.id.following_arrives_in_minutes) TextView mFollowingArrivesMinutesView;
    @Bind(R.id.following_travel_quality) TextView mFollowingTravelQualityView;
    @Bind(R.id.following_bus_number) TextView mFollowingBusNumber;
    @Bind(R.id.following_expected_arrival) TextView mFollowingExpectedArrival;
    @Bind(R.id.following_planned_arrival) TextView mFollowingPlannedArrival;

    @Override
    protected int provideLayoutResource() {
        return R.layout.fragment_arrival_prediction;
    }

    public static ArrivalPredictionFragment newInstance(@NonNull ArrivalPrediction data) {
        Timber.d("Creating new instance of %s", LOG);
        ArrivalPredictionFragment fragment = new ArrivalPredictionFragment();

        Bundle args = new Bundle();

        Timber.d("Assigning data arguments with value %s", data.toString());
        args.putParcelable(ARG_DATA, Parcels.wrap(data));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Timber.d("Creating view");

        mArrivalPrediction = Parcels.unwrap(getArguments().getParcelable(ARG_DATA));

        Timber.d("Data retrieved with value %s", mArrivalPrediction.toString());

        if (mArrivalPrediction.getNext() == null
                && mArrivalPrediction.getFollowing() == null) {

        } else {
            setUpNextTravelCard(mArrivalPrediction);
            setUpFollowingTravelCard(mArrivalPrediction);
        }
    }

    private void setUpNextTravelCard(@NonNull ArrivalPrediction arrivalPrediction) {
        Timber.d("Binding next travel received to view");

        final BusTravel next = arrivalPrediction.getNext();

        mNextArrivesMinutesView.setText(
                String.valueOf(next.getMinutesToArrive()));
        mNextTravelQualityView.setText(
                next.getQuality());
        mNextBusNumber.setText(
                next.getBusNumber());
        mNextExpectedArrival.setText(
                parseAndFormatDate(next.getExpectedArrivalTime()));
        mNextPlannedArrival.setText(
                parseAndFormatDate(next.getPlannedArrivalTime()));
    }

    @SuppressWarnings("ConstantConditions")
    private void setUpFollowingTravelCard(@NonNull ArrivalPrediction arrivalPrediction) {
        Timber.d("Binding following travel received to view");

        final BusTravel following = arrivalPrediction.getFollowing();
        if (following == null) {
            Timber.d("No following travel found");
            ButterKnife.findById(getView(), R.id.following_travel).setVisibility(View.GONE);
        } else {
            mFollowingArrivesMinutesView.setText(
                    String.valueOf(following.getMinutesToArrive()));
            mFollowingTravelQualityView.setText(
                    following.getQuality());
            mFollowingBusNumber.setText(
                    following.getBusNumber());
            mFollowingExpectedArrival.setText(
                    parseAndFormatDate(following.getExpectedArrivalTime()));
            mFollowingPlannedArrival.setText(
                    parseAndFormatDate(following.getPlannedArrivalTime()));
        }
    }

    final SimpleDateFormat mDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",
            Locale.getDefault());

    private String parseAndFormatDate(@NonNull String dateString) {
        try {
            Date date = mDateFormatter.parse(dateString);

            return DateUtils.getRelativeTimeSpanString(date.getTime(),
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            Timber.e("Error parsing the data string %s", dateString);
            return dateString;
        }
    }

}
