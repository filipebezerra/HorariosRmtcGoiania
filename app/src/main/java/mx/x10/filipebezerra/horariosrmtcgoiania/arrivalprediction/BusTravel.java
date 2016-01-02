package mx.x10.filipebezerra.horariosrmtcgoiania.arrivalprediction;

import android.support.annotation.NonNull;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.model.BusTravelModel;
import org.parceler.Parcel;

/**
 * Bus travel model.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 29/12/2015
 * @since 0.1.0
 */
@Parcel(Parcel.Serialization.BEAN)
public class BusTravel {
    private String mQuality;

    private String mBusNumber;

    private String mPlannedArrivalTime;

    private String mExpectedArrivalTime;

    private Long mMinutesToArrive;

    public BusTravel() {
        //required
    }

    @SuppressWarnings("ConstantConditions")
    public static BusTravel fromModel(@NonNull BusTravelModel model) {
        if (model == null) {
            return null;
        }

        return new BusTravel()
                .setQuality(model.quality)
                .setBusNumber(model.busNumber)
                .setPlannedArrivalTime(model.plannedArrivalTime)
                .setExpectedArrivalTime(model.expectedArrivalTime)
                .setMinutesToArrive(model.minutesToArrive);
    }

    public String getQuality() {
        return mQuality;
    }

    public BusTravel setQuality(String quality) {
        mQuality = quality;
        return this;
    }

    public String getBusNumber() {
        return mBusNumber;
    }

    public BusTravel setBusNumber(String busNumber) {
        mBusNumber = busNumber;
        return this;
    }

    public String getPlannedArrivalTime() {
        return mPlannedArrivalTime;
    }

    public BusTravel setPlannedArrivalTime(String plannedArrivalTime) {
        mPlannedArrivalTime = plannedArrivalTime;
        return this;
    }

    public String getExpectedArrivalTime() {
        return mExpectedArrivalTime;
    }

    public BusTravel setExpectedArrivalTime(String expectedArrivalTime) {
        mExpectedArrivalTime = expectedArrivalTime;
        return this;
    }

    public Long getMinutesToArrive() {
        return mMinutesToArrive;
    }

    public BusTravel setMinutesToArrive(Long minutesToArrive) {
        mMinutesToArrive = minutesToArrive;
        return this;
    }
}
