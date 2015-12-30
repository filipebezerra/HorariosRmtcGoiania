package mx.x10.filipebezerra.horariosrmtcgoiania.busstop;

import android.support.annotation.NonNull;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import mx.x10.filipebezerra.horariosrmtcgoiania.api.model.BusStopModel;
import mx.x10.filipebezerra.horariosrmtcgoiania.busline.BusLine;

/**
 * Bus stop model.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 29/12/2015
 * @since 0.1.0
 */
@Parcel(Parcel.Serialization.BEAN)
public class BusStop {
    private String mId;

    private String mAddress;

    private String mLatitude;

    private String mLongitude;

    private List<BusLine> mLines;

    public BusStop() {
        // required
    }

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
}
