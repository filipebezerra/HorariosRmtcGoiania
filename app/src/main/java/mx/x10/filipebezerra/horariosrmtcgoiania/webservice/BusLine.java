package mx.x10.filipebezerra.horariosrmtcgoiania.webservice;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

/**
 * Class representation of a bus line.
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 03/04/2016
 * @since 3.0.0
 */
@ParcelablePlease
public class BusLine implements Parcelable {
    @SerializedName("Numero")
    public String number;

    @SerializedName("Itinerario")
    public String itinerary;

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
