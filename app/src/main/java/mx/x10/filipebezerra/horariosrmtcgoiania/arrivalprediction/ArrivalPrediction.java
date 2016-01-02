package mx.x10.filipebezerra.horariosrmtcgoiania.arrivalprediction;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.model.ArrivalPredictionModel;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.response.ResponseUtil;

/**
 * Arrival prediction model.
 *
 * @author Filipe Bezerra
 * @version 0.3.0, 02/01/2016
 * @since 0.1.0
 */
@ParcelablePlease
public class ArrivalPrediction implements Parcelable {
    String mLineNumber;

    String mDestination;

    BusTravel mNext;

    BusTravel mFollowing;

    public static ArrivalPrediction fromModel(@NonNull ArrivalPredictionModel model) {
        return new ArrivalPrediction()
                .setLineNumber(model.lineNumber)
                .setDestination(model.destination)
                .setNext(BusTravel.fromModel(model.next))
                .setFollowing(BusTravel.fromModel(model.following));
    }

    public String getLineNumber() {
        return mLineNumber;
    }

    public ArrivalPrediction setLineNumber(String lineNumber) {
        this.mLineNumber = lineNumber;
        return this;
    }

    public String getDestination() {
        return mDestination;
    }

    public ArrivalPrediction setDestination(String destination) {
        this.mDestination = destination;
        return this;
    }

    public BusTravel getNext() {
        return mNext;
    }

    public ArrivalPrediction setNext(BusTravel next) {
        this.mNext = next;
        return this;
    }

    public BusTravel getFollowing() {
        return mFollowing;
    }

    public ArrivalPrediction setFollowing(BusTravel following) {
        this.mFollowing = following;
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s \n%s", getClass().getSimpleName(),
                ResponseUtil.toPrintable(this));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ArrivalPredictionParcelablePlease.writeToParcel(this, dest, flags);
    }

    public static final Creator<ArrivalPrediction> CREATOR = new Creator<ArrivalPrediction>() {
        public ArrivalPrediction createFromParcel(Parcel source) {
            ArrivalPrediction target = new ArrivalPrediction();
            ArrivalPredictionParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public ArrivalPrediction[] newArray(int size) {
            return new ArrivalPrediction[size];
        }
    };
}
