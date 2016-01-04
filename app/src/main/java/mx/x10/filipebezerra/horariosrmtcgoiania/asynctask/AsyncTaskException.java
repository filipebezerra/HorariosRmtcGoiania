package mx.x10.filipebezerra.horariosrmtcgoiania.asynctask;

import android.support.annotation.NonNull;

/**
 * Async task exception.
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 04/01/2016
 * @since 3.0.0
 */
public class AsyncTaskException extends Exception {
    public AsyncTaskException(String error) {
        super(error);
    }

    public AsyncTaskException(Throwable throwable) {
        super(throwable);
    }

    public static AsyncTaskException of(@NonNull String error) {
        return new AsyncTaskException(error);
    }

    public static AsyncTaskException of(@NonNull Throwable error) {
        return new AsyncTaskException(error);
    }
}
