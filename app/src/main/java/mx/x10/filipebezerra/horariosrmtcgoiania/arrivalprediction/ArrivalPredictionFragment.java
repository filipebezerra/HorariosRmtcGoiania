package mx.x10.filipebezerra.horariosrmtcgoiania.arrivalprediction;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import java.text.ParseException;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.base.BaseFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.drawable.DrawableHelper;
import timber.log.Timber;

import static android.os.Build.VERSION;
import static android.os.Build.VERSION_CODES;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static mx.x10.filipebezerra.horariosrmtcgoiania.application.Constants.API_DATE_TIME_FORMATTER;
import static mx.x10.filipebezerra.horariosrmtcgoiania.application.Constants.DEFAULT_TIME_FORMATTER;

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
    public static final String QUALIDADE_PLANILHA_DE_HORARIOS = "Planilha de Horários";

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
            if (nextTravel.getQuality().equalsIgnoreCase(QUALIDADE_PLANILHA_DE_HORARIOS)) {
                changeDrawable(mNextTravelView, R.drawable.ic_timetable_24dp);
            }

            // setting up next travel icon color
            changeDrawableColor(mNextTravelView, R.color.colorPrimary);

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
            if (followingTravel.getQuality().equalsIgnoreCase(QUALIDADE_PLANILHA_DE_HORARIOS)) {
                changeDrawable(mFollowingTravelView, R.drawable.ic_timetable_24dp);
            }

            // setting up following travel icon color
            changeDrawableColor(mFollowingTravelView, R.color.colorAccent);

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

    private void changeDrawable(@NonNull TextView textView, @DrawableRes int drawableRes) {
        final Drawable drawable = ContextCompat.getDrawable(getContext(), drawableRes);
        textView.setCompoundDrawables(drawable, null, null, null);

        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setCompoundDrawablesRelative(drawable, null, null, null);
        }
    }

    private void changeDrawableColor(@NonNull TextView textView, @ColorRes int colorRes) {
        final Drawable leftDrawable = textView.getCompoundDrawables()[0];

        if (leftDrawable != null) {
            DrawableHelper.tint(getContext(), colorRes, leftDrawable);
        }

        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
            final Drawable leftRelativeDrawable = textView.getCompoundDrawablesRelative()[0];

            if (leftRelativeDrawable != null) {
                DrawableHelper.tint(getContext(), colorRes, leftRelativeDrawable);
            }
        }
    }

    private String toTimeFormat(@NonNull String dateString) {
        try {
            return DEFAULT_TIME_FORMATTER.format(
                    API_DATE_TIME_FORMATTER.parse(dateString));
        } catch (ParseException e) {
            return dateString;
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