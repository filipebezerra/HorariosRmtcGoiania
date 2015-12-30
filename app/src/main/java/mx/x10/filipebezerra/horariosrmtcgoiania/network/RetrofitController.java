package mx.x10.filipebezerra.horariosrmtcgoiania.network;

import android.content.Context;
import android.support.annotation.NonNull;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import java.io.File;
import java.util.concurrent.TimeUnit;
import mx.x10.filipebezerra.horariosrmtcgoiania.api.service.BusStopService;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * {@link Retrofit} helper class.
 *
 * @author Filipe Bezerra
 * @version 0.1.0, 30/11/2015
 * @since 0.1.0
 */
public class RetrofitController {
    private static final long HTTP_CACHE_SIZE = 10 * 1024 * 1024;
    private static final String HTTP_CACHE_FILE_NAME = "http";
    private static BusStopService sService;

    private RetrofitController() {
        // no instances
    }

    public static BusStopService instance(@NonNull Context context) {
        if (sService == null) {
            final Cache cache = new Cache(new File(context.getCacheDir(), HTTP_CACHE_FILE_NAME),
                    HTTP_CACHE_SIZE);

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            final OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
            okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
            okHttpClient.setWriteTimeout(30, TimeUnit.SECONDS);
            okHttpClient.setCache(cache);
            okHttpClient.interceptors().add(interceptor);

            Retrofit retrofit = new Retrofit.Builder().
                    baseUrl(BusStopService.BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).
                    addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                    client(okHttpClient).
                    build();

            sService = retrofit.create(BusStopService.class);
        }
        return sService;
    }
}
