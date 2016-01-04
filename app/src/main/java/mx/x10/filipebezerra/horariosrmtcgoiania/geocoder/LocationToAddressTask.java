package mx.x10.filipebezerra.horariosrmtcgoiania.geocoder;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import java.io.IOException;
import java.util.List;
import mx.x10.filipebezerra.horariosrmtcgoiania.asynctask.AbstractAsyncTask;
import mx.x10.filipebezerra.horariosrmtcgoiania.asynctask.AsyncTaskCallback;

import static mx.x10.filipebezerra.horariosrmtcgoiania.asynctask.AsyncTaskException.of;

/**
 * {@link Location} to {@link Address} async task converter.
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 04/01/2016
 * @since 3.0.0
 */
public class LocationToAddressTask extends AbstractAsyncTask<Location, Void, Address> {
    @NonNull private final Context mContext;

    public LocationToAddressTask(@NonNull AsyncTaskCallback<Address> callback,
            @NonNull Context context) {
        super(callback);
        mContext = context;
    }

    @Override
    protected Address doInBackground(Location... params) {
        if (!checkParamsNotNull(params)) {
            return null;
        }

        if (!Geocoder.isPresent()) {
            mException = of(new NullPointerException("Geocoder is not present"));
            return null;
        }

        final Geocoder geocoder = new Geocoder(mContext);
        try {
            final Location location = params[0];
            final List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);

            return addresses.get(0);
        } catch (IOException e) {
            mException = of(e);
            return null;
        }
    }
}
