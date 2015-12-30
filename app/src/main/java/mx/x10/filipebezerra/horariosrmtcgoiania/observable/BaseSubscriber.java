package mx.x10.filipebezerra.horariosrmtcgoiania.observable;

import android.support.annotation.NonNull;

import rx.Subscriber;
import timber.log.Timber;

/**
 * Base {@link rx.Subscriber} for all subscribers of this application.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 29/12/2015
 * @since 0.1.0
 */
public abstract class BaseSubscriber<T> extends Subscriber<T> {
    private static final String LOG = BaseSubscriber.class.getSimpleName();

    protected SubscriberDelegate<T> mDelegate;

    public BaseSubscriber(@NonNull SubscriberDelegate<T> delegate) {
        Timber.tag(LOG);
        mDelegate = delegate;
    }

    @Override
    public void onStart() {
        Timber.i("Started, now the Subscriber and Observable are connected.");
        mDelegate.onStart();
    }

    @Override
    public void onNext(T t) {
        Timber.i("Received new item to observe.");
        if (t == null) {
            onError(new NullPointerException("The item emitted by the Observable was null"));
        } else {
            mDelegate.onNext(t);
        }
    }

    @Override
    public void onCompleted() {
        Timber.i("Done, the Observable has finished.");
        mDelegate.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        Timber.i("Error, the Observable has experienced an error condition.");
        mDelegate.onError(e);
    }
}
