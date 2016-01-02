package mx.x10.filipebezerra.horariosrmtcgoiania.busstop;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;
import java.util.ArrayList;
import java.util.List;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.model.BusStopModel;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.response.ResponseUtil;
import mx.x10.filipebezerra.horariosrmtcgoiania.busline.BusLine;

/**
 * Bus stop model.
 *
 * @author Filipe Bezerra
 * @version 0.3.0, 02/01/2016
 * @since 0.1.0
 */
@ParcelablePlease
public class BusStop implements Parcelable {
    String mId;

    String mAddress;

    String mLatitude;

    String mLongitude;

    List<BusLine> mLines;

    public static BusStop fromModel(@NonNull BusStopModel model) {
        return new BusStop()
                .setId(model.id)
                .setAddress(model.address)
                .setLatitude(model.latitude)
                .setLongitude(model.longitude)
                .setLines(BusLine.fromModel(model.lines));
    }

    public static List<BusStop> fromModel(@NonNull List<BusStopModel> models) {
        final List<BusStop> busLines = new ArrayList<>();

        for (BusStopModel model : models) {
            busLines.add(BusStop.fromModel(model));
        }

        return busLines;
    }

    public String getId() {
        return mId;
    }

    public BusStop setId(String id) {
        mId = id;
        return this;
    }

    public String getAddress() {
        return mAddress;
    }

    public BusStop setAddress(String address) {
        mAddress = address;
        return this;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public BusStop setLatitude(String latitude) {
        mLatitude = latitude;
        return this;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public BusStop setLongitude(String longitude) {
        mLongitude = longitude;
        return this;
    }

    public List<BusLine> getLines() {
        return mLines;
    }

    public BusStop setLines(List<BusLine> lines) {
        mLines = lines;
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
        BusStopParcelablePlease.writeToParcel(this, dest, flags);
    }

    public static final Creator<BusStop> CREATOR = new Creator<BusStop>() {
        public BusStop createFromParcel(Parcel source) {
            BusStop target = new BusStop();
            BusStopParcelablePlease.readFromParcel(target, source);
            return target;
        }

        public BusStop[] newArray(int size) {
            return new BusStop[size];
        }
    };
}
