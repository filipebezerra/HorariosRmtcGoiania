package mx.x10.filipebezerra.horariosrmtcgoiania.eventbus;

import mx.x10.filipebezerra.horariosrmtcgoiania.arrivalprediction.ArrivalPrediction;
import mx.x10.filipebezerra.horariosrmtcgoiania.busstop.BusStop;

/**
 * An event containing a {@link BusStop} object and an {@link ArrivalPrediction} object.
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 05/01/2016
 * @since 3.0.0
 */
public class BusStopWithArrivalPrediction {
    private final BusStop mBusStop;
    private final ArrivalPrediction mArrivalPrediction;

    public BusStopWithArrivalPrediction(BusStop busStop, ArrivalPrediction arrivalPrediction) {
        mBusStop = busStop;
        mArrivalPrediction = arrivalPrediction;
    }

    public static BusStopWithArrivalPrediction create(
            BusStop busStop, ArrivalPrediction arrivalPrediction) {
        return new BusStopWithArrivalPrediction(busStop, arrivalPrediction);
    }

    public BusStop getBusStop() {
        return mBusStop;
    }

    public ArrivalPrediction getArrivalPrediction() {
        return mArrivalPrediction;
    }

    @Override
    public String toString() {
        return String.format("Contains %s and %s", getBusStop().toString(),
                getArrivalPrediction().toString());
    }
}
