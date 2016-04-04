package mx.x10.filipebezerra.horariosrmtcgoiania.webservice;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representation of a bus station, or a bus stop.
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 03/04/2016
 * @since 3.0.0
 */
@ParcelablePlease
public class BusStation implements Parcelable {
    @SerializedName("IdPontoParada")
    public String busStationId;

    @SerializedName("Endereco")
    public String address;

    @SerializedName("Latitude")
    public String latitude;

    @SerializedName("Longitude")
    public String longitude;

    @SerializedName("Linhas")
    public List<BusLine> lines = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        BusStationParcelablePlease.writeToParcel(this, dest, flags);
    }

    public static final Creator<BusStation> CREATOR = new Creator<BusStation>() {
        public BusStation createFromParcel(Parcel source) {
            BusStation target = new BusStation();
            BusStationParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public BusStation[] newArray(int size) {
            return new BusStation[size];
        }
    };
}
