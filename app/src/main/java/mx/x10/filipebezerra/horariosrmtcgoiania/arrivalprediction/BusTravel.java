package mx.x10.filipebezerra.horariosrmtcgoiania.arrivalprediction;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.model.BusTravelModel;

/**
 * Bus travel model.
 *
 * @author Filipe Bezerra
 * @version 0.3.0, 02/01/2016
 * @since 0.1.0
 */
@ParcelablePlease
public class BusTravel implements Parcelable {
    String mQuality;

    String mBusNumber;

    String mPlannedArrivalTime;

    String mExpectedArrivalTime;

    Long mMinutesToArrive;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        BusTravelParcelablePlease.writeToParcel(this, dest, flags);
    }

    public static final Creator<BusTravel> CREATOR = new Creator<BusTravel>() {
        public BusTravel createFromParcel(Parcel source) {
            BusTravel target = new BusTravel();
            BusTravelParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public BusTravel[] newArray(int size) {
            return new BusTravel[size];
        }
    };
}
