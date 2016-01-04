package mx.x10.filipebezerra.horariosrmtcgoiania.asynctask;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import java.util.List;

/**
 * Abstract async task.
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 04/01/2016
 * @since 3.0.0
 */
public abstract class AbstractAsyncTask<Params, Progress, Result>
        extends AsyncTask<Params, Progress, Result> {

    protected AsyncTaskCallback<Result> mCallback;

    protected AsyncTaskException mException;

    public AbstractAsyncTask(@NonNull AsyncTaskCallback<Result> callback) {
        mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mCallback.onBegin();
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (mException == null) {
            if (result == null ||
                    (result instanceof List && ((List)result).isEmpty() )) {
                mCallback.onResultNothing();
            } else {
                mCallback.onSuccess(result);
            }
        } else {
            mCallback.onError(mException);
        }
    }

    @SafeVarargs
    protected final boolean checkParamsNotNull(Params... params) {
        if (params == null) {
            mException = AsyncTaskException.of(new NullPointerException("Parâmetro está nulo."));
            return false;
        }

        return true;
    }
}
