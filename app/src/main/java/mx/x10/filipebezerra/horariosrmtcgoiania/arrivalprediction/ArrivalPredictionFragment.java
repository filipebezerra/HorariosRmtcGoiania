package mx.x10.filipebezerra.horariosrmtcgoiania.arrivalprediction;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.base.BaseFragment;
import timber.log.Timber;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static mx.x10.filipebezerra.horariosrmtcgoiania.date.DateHelper.toTimeFormat;
import static mx.x10.filipebezerra.horariosrmtcgoiania.drawable.DrawableHelper.changeLeftCompoundDrawable;
import static mx.x10.filipebezerra.horariosrmtcgoiania.drawable.DrawableHelper.changeLeftDrawableColor;

/**
 * User presentation of {@link ArrivalPrediction}.
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 02/01/2016
 * @since 3.0.0
 */
public class ArrivalPredictionFragment extends BaseFragment {

    // logging purposes
    private static final String LOG = ArrivalPredictionFragment.class.getSimpleName();

    // User data handled by this fragment
    private static final String ARG_DATA = "ArrivalPrediction";

    // State of user data handled by this fragment
    private static final String STATE_ARG_DATA = "State_" + ARG_DATA;

    // timetable quality from arrival prediction
    private static final String TIMETABLE_QUALITY = "Planilha de Horários";

    // Data representation handled by this fragment
    private ArrivalPrediction mArrivalPrediction;

    @Bind(R.id.root_layout)
    protected NestedScrollView mRootLayout;

    @Bind(R.id.line_itinerary)
    protected TextView mLineItineraryView;

    @Bind(R.id.line_number)
    protected TextView mLineNumberView;

    @Bind(R.id.next_travel_label)
    protected TextView mNextTravelView;

    @Bind(R.id.next_bus_number)
    protected TextView mNextBusNumberView;

    @Bind(R.id.next_expected_arrival_time)
    protected TextView mNextExpectedArrivalTimeView;

    @Bind(R.id.next_countdown_timer)
    protected TextView mNextCountdownTimerView;

    @Bind(R.id.following_travel_label)
    protected TextView mFollowingTravelView;

    @Bind(R.id.following_bus_number)
    protected TextView mFollowingBusNumberView;

    @Bind(R.id.following_expected_arrival_time)
    protected TextView mFollowingExpectedArrivalTimeView;

    @Bind(R.id.following_countdown_timer)
    protected TextView mFollowingCountdownTimerView;

    @Override
    protected int provideLayoutResource() {
        return R.layout.fragment_arrival_prediction;
    }

    public static ArrivalPredictionFragment newInstance(@NonNull ArrivalPrediction data) {
        Timber.d("Creating new instance of %s", LOG);
        ArrivalPredictionFragment fragment = new ArrivalPredictionFragment();

        Bundle args = new Bundle();

        Timber.d("Assigning data arguments with value %s", data.toString());
        args.putParcelable(ARG_DATA, data);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getArguments() == null)
                || !(getArguments().containsKey(ARG_DATA))) {
            throw new IllegalStateException(
                    "This fragment must contains the argument 'ARG_DATA'.");
        } else if (!(getArguments().get(ARG_DATA) instanceof Parcelable)
                || getArguments().getParcelable(ARG_DATA) == null) {
            throw new IllegalArgumentException(
                    "The argument 'ARG_DATA' must be a android.os.Parcelable instance.");
        }
    }

    @Override
    public void onActivityCreated(Bundle inState) {
        super.onActivityCreated(inState);

        if (inState != null) {
            mArrivalPrediction = inState.getParcelable(STATE_ARG_DATA);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Timber.d("Creating view");

        mArrivalPrediction = getArguments().getParcelable(ARG_DATA);

        Timber.d("Data retrieved with value %s", mArrivalPrediction.toString());

        setupLineInfoCard(mArrivalPrediction.getLineNumber(),
                mArrivalPrediction.getDestination());
        setupNextTravelCard(mArrivalPrediction.getNext());
        setupFollowingTravelCard(mArrivalPrediction.getFollowing());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_ARG_DATA, mArrivalPrediction);
    }

    private void setupLineInfoCard(String lineNumber, String destination) {
        Timber.d("Binding line info received to view");

        mLineNumberView.setText(lineNumber);
        mLineItineraryView.setText(destination);
    }

    private void setupNextTravelCard(BusTravel nextTravel) {
        Timber.d("Binding next travel received to view");

        if (nextTravel == null) {
            Timber.d("Next travel not found");
            ButterKnife.findById(mRootLayout, R.id.next_travel)
                    .setVisibility(View.GONE);
        } else {
            // setting up next travel icon for different quality
            if (nextTravel.getQuality().equalsIgnoreCase(TIMETABLE_QUALITY)) {
                changeLeftCompoundDrawable(getContext(), mNextTravelView,
                        R.drawable.ic_timetable_24dp);
            }

            // setting up next travel icon color
            changeLeftDrawableColor(getContext(), mNextTravelView, R.color.colorPrimary);

            // setting up next bus number text
            mNextBusNumberView.setText(getString(
                    R.string.bus_number, nextTravel.getBusNumber()));

            // setting up next expected arrival time text
            mNextExpectedArrivalTimeView.setText(getString(
                    R.string.expected_arrival_time,
                    toTimeFormat(nextTravel.getExpectedArrivalTime())));

            // setting up next countdown timer
            new CountDownTimer(
                    MINUTES.toMillis(nextTravel.getMinutesToArrive()),
                    1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (isVisible()) {
                        mNextCountdownTimerView.setText(toRemainingTimeFormat(millisUntilFinished));
                    }
                }

                @Override
                public void onFinish() {
                    if (isVisible()) {
                        mNextCountdownTimerView.setText(R.string.bus_has_arrived);
                    }
                }
            }.start();
        }
    }

    private void setupFollowingTravelCard(BusTravel followingTravel) {
        Timber.d("Binding following travel received to view");

        if (followingTravel == null) {
            Timber.d("Following travel not found");
            ButterKnife.findById(mRootLayout, R.id.following_travel)
                    .setVisibility(View.GONE);
        } else {
            // setting up following travel icon for different quality
            if (followingTravel.getQuality().equalsIgnoreCase(TIMETABLE_QUALITY)) {
                changeLeftCompoundDrawable(getContext(), mFollowingTravelView,
                        R.drawable.ic_timetable_24dp);
            }

            // setting up following travel icon color
            changeLeftDrawableColor(getContext(), mFollowingTravelView, R.color.colorAccent);

            // setting up following bus number text
            mFollowingBusNumberView.setText(getString(R.string.bus_number,
                    followingTravel.getBusNumber()));

            // setting up following expected arrival time text
            mFollowingExpectedArrivalTimeView.setText(getString(
                    R.string.expected_arrival_time,
                    toTimeFormat(followingTravel.getExpectedArrivalTime())));

            // setting up following countdown timer
            new CountDownTimer(
                    MINUTES.toMillis(followingTravel.getMinutesToArrive()),
                    1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (isVisible()) {
                        mFollowingCountdownTimerView.setText(
                                toRemainingTimeFormat(millisUntilFinished));
                    }
                }

                @Override
                public void onFinish() {
                    if (isVisible()) {
                        mFollowingCountdownTimerView.setText(getString(R.string.bus_has_arrived));
                    }
                }
            }.start();
        }
    }

    private String toRemainingTimeFormat(long millisUntilFinished) {
        final long hours = MILLISECONDS.toHours(millisUntilFinished);
        final long minutes = MILLISECONDS.toMinutes(millisUntilFinished) -
                HOURS.toMinutes(MILLISECONDS.toHours(millisUntilFinished));
        final long seconds = MILLISECONDS.toSeconds(millisUntilFinished) -
                MINUTES.toSeconds(MILLISECONDS.toMinutes(millisUntilFinished));

        if (hours == 0 && minutes == 0) {
            return getString(R.string.countdown_fewer_than_minute);
        } else {
            if (hours == 0) {
                return getString(R.string.countdown_in_minutes, minutes, seconds);
            } else {
                return getString(R.string.countdown_in_hours, hours, minutes, seconds);
            }
        }
    }
}