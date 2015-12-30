package mx.x10.filipebezerra.horariosrmtcgoiania.arrivalprediction;

import android.support.annotation.NonNull;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.model.ArrivalPredictionModel;
import org.parceler.Parcel;

/**
 * Arrival prediction model.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 29/12/2015
 * @since 0.1.0
 */
@Parcel(Parcel.Serialization.BEAN)
public class ArrivalPrediction {
    private String mLineNumber;

    private String mDestination;

    private BusTravel mNext;

    private BusTravel mFollowing;

    public ArrivalPrediction() {
        //required
    }

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
}
