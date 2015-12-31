package mx.x10.filipebezerra.horariosrmtcgoiania.busline;

import android.support.annotation.NonNull;

import mx.x10.filipebezerra.horariosrmtcgoiania.api.response.ResponseUtil;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import mx.x10.filipebezerra.horariosrmtcgoiania.api.model.BusLineModel;

/**
 * Bus line model.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 29/12/2015
 * @since 0.1.0
 */
@Parcel(Parcel.Serialization.BEAN)
public class BusLine {
    private String number;

    private String itinerary;

    public BusLine() {
        // required
    }

    public static BusLine fromModel(@NonNull BusLineModel model) {
        return new BusLine()
                .setNumber(model.number)
                .setItinerary(model.itinerary);
    }

    public static List<BusLine> fromModel(@NonNull List<BusLineModel> models) {
        final List<BusLine> busLines = new ArrayList<>();

        for (BusLineModel model : models) {
            busLines.add(BusLine.fromModel(model));
        }

        return busLines;
    }

    public String getNumber() {
        return number;
    }

    public BusLine setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getItinerary() {
        return itinerary;
    }

    public BusLine setItinerary(String itinerary) {
        this.itinerary = itinerary;
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s \n%s", getClass().getSimpleName(),
                ResponseUtil.toPrintable(this));
    }
}
