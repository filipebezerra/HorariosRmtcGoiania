package mx.x10.filipebezerra.horariosrmtcgoiania.busline;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;
import java.util.ArrayList;
import java.util.List;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.model.BusLineModel;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.response.ResponseUtil;

/**
 * Bus line model.
 *
 * @author Filipe Bezerra
 * @version 0.3.0, 02/01/2016
 * @since 0.1.0
 */
@ParcelablePlease
public class BusLine implements Parcelable {
    String number;

    String itinerary;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        BusLineParcelablePlease.writeToParcel(this, dest, flags);
    }

    public static final Creator<BusLine> CREATOR = new Creator<BusLine>() {
        public BusLine createFromParcel(Parcel source) {
            BusLine target = new BusLine();
            BusLineParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public BusLine[] newArray(int size) {
            return new BusLine[size];
        }
    };
}
