package mx.x10.filipebezerra.horariosrmtcgoiania.api.subscriber;

import android.support.annotation.NonNull;

import mx.x10.filipebezerra.horariosrmtcgoiania.api.response.BaseApiResponse;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.response.ResponseUtil;
import mx.x10.filipebezerra.horariosrmtcgoiania.observable.BaseSubscriber;
import mx.x10.filipebezerra.horariosrmtcgoiania.observable.SubscriberDelegate;
import timber.log.Timber;

/**
 * Asynchronous subscriber from API observers.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 29/12/2015
 * @since 0.1.0
 */
public class ApiSubscriber<T extends BaseApiResponse> extends BaseSubscriber<T> {
    public ApiSubscriber(@NonNull SubscriberDelegate<T> delegate) {
        super(delegate);
    }

    @Override
    public void onNext(T t) {
        super.onNext(t);
        Timber.i("Requesting bus stop with lines result with \n\t%s",
                ResponseUtil.toPrintable(t));
    }
}
