package mx.x10.filipebezerra.horariosrmtcgoiania.observable;

/**
 * {@link BaseSubscriber}s must use this to delegate the result.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 29/12/2015
 * @since 0.1.0
 */
public interface SubscriberDelegate<T> {
    void onStart();

    void onNext(T observable);

    void onCompleted();

    void onError(Throwable e);
}
