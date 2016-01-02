package mx.x10.filipebezerra.horariosrmtcgoiania.playservices;

import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import mx.x10.filipebezerra.horariosrmtcgoiania.eventbus.BusProvider;
import timber.log.Timber;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static mx.x10.filipebezerra.horariosrmtcgoiania.application.Constants.REQUEST_LOCATION;
import static mx.x10.filipebezerra.horariosrmtcgoiania.application.Constants.REQUEST_RESOLVE_ERROR;

/**
 * Helper containing all configurations for integration with Google Play Services.
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 30/12/2015
 * @since 3.0.0
 */
public class PlayServicesHelper
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = PlayServicesHelper.class.getSimpleName();

    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError;

    public static final String STATE_RESOLVING_ERROR = "State_ResolvingError";
    public static final String STATE_LAST_LOCATION = "State_LastLocation";

    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;

    @NonNull private final Activity mActivity;

    @NonNull private final PlayServicesCallbacks mCallbacks;

    public enum PlayServicesPermission {
        FINE_LOCATION(ACCESS_FINE_LOCATION);

        private String mName;

        PlayServicesPermission(String name) {
            mName = name;
        }

        public String getName() {
            return mName;
        }
    }

    public interface PlayServicesCallbacks {
        void onLocationIsAvailable(@NonNull Location location);
        void onConnectionError(int errorCode);
        void onRequestLocationPermission(PlayServicesPermission permission);
    }

    public PlayServicesHelper(@NonNull Activity activity, @Nullable Bundle activityState,
            @NonNull PlayServicesCallbacks callbacks) {
        Timber.tag(TAG);

        mActivity = activity;
        mCallbacks = callbacks;

        buildGoogleApiClient();

        if (activityState != null) {
            mResolvingError = activityState.getBoolean(STATE_RESOLVING_ERROR, false);
            mLastLocation = activityState.getParcelable(STATE_LAST_LOCATION);
            Timber.i("Saved state was restored. Resolving error = %b", mResolvingError);
        }
    }

    private synchronized void buildGoogleApiClient() {
        Timber.d("Building google api client");
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void connect() {
        Timber.d("Caller call connect() to play services");
        if (!mResolvingError) {
            // Make sure the app is not already connected or attempting to connect
            if (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()) {
                BusProvider.register(this);
                mGoogleApiClient.connect();
                Timber.i("Connecting to play services");
            } else {
                Timber.i("Play services is already connecting or connected");
            }
        } else {
            Timber.i("Is resolving error. Will not connect to play services");
        }
    }

    public void disconnect() {
        Timber.d("Caller call disconnect() to play services");
        if (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.disconnect();
            Timber.i("Disconnecting from play services");
            BusProvider.unregister(this);
        } else {
            Timber.i("Play services is already disconnected");
        }
    }

    public boolean isResolvingError() {
        return mResolvingError;
    }

    public void setResolvingError(boolean isResolving) {
        Timber.d("Caller call setResolvingError(%b) to play services", isResolving);
        mResolvingError = isResolving;
    }

    public Location getLastLocation() {
        return mLastLocation;
    }

    private void produceLocationIfAvailable() {
        Timber.d("Checking requirements to produce location");
        if (checkSelfPermission(mActivity, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            // Check Permissions Now
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                    ACCESS_FINE_LOCATION)) {
                Timber.i("Permission not yet presented to user");
                produceRequestLocationPermission();
            } else {
                Timber.i("Requesting permission to access location");
                ActivityCompat.requestPermissions(
                        mActivity, new String[]{ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
            }

            Timber.i("No location was produced");
        } else {
            Timber.i("All requirements was verified, now requesting last location to provider");
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation == null) {
                Timber.i("Last location was not available");
            } else {
                Timber.i("Last location is %s", mLastLocation.toString());
                mCallbacks.onLocationIsAvailable(mLastLocation);
            }
        }
    }

    private void produceRequestLocationPermission() {
        Timber.i("Producing request location permission");
        mCallbacks.onRequestLocationPermission(PlayServicesPermission.FINE_LOCATION);
    }

    private void produceConnectionError(int errorCode) {
        Timber.i("Producing connection error with code %d", errorCode);

        if (errorCode != ConnectionResult.SUCCESS) {
            mCallbacks.onConnectionError(errorCode);
        }
    }

    public void setRequestedPermissionGranted(PlayServicesPermission permission) {
        Timber.d("Caller call setRequestedPermissionGranted() with permission %s",
                permission.getName());
        switch (permission) {
            case FINE_LOCATION:
                produceLocationIfAvailable();
                break;
        }
    }

    @Override
    public void onConnected(Bundle hint) {
        Timber.i("Connected with play services");
        produceLocationIfAvailable();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Timber.i("Connection with play services suspended with cause %d", cause);
        connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Timber.i("Connection with play services failed with error %d:%s",
                result.getErrorCode(), result.getErrorMessage());

        if (!mResolvingError) {
            if (result.hasResolution()) {
                Timber.i("The connection error has resolution, starting resolution");
                try {
                    mResolvingError = true;
                    result.startResolutionForResult(mActivity, REQUEST_RESOLVE_ERROR);
                } catch (IntentSender.SendIntentException e) {
                    Timber.i("There was an error with the resolution intent. Trying connect");
                    mGoogleApiClient.connect();
                }
            } else {
                Timber.i("The connection error hasn't resolution");
                produceConnectionError(result.getErrorCode());
            }
        } else {
            Timber.d("The connection error is already resolving");
        }
    }
}
