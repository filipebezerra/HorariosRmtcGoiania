package mx.x10.filipebezerra.horariosrmtcgoiania.asynctask;

/**
 * Async task callback.
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 04/01/2016
 * @since 3.0.0
 */
public interface AsyncTaskCallback<R> {
    void onBegin();
    void onSuccess(R result);
    void onResultNothing();
    void onError(AsyncTaskException e);
}
