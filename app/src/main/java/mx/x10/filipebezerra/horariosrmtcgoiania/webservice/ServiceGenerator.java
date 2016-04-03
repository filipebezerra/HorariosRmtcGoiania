package mx.x10.filipebezerra.horariosrmtcgoiania.webservice;

import android.content.Context;
import android.support.annotation.NonNull;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import java.io.File;
import java.util.concurrent.TimeUnit;
import mx.x10.filipebezerra.horariosrmtcgoiania.BuildConfig;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Proxy class to instantiate classes which consumes Web services resources.
 *
 * @author Filipe Bezerra
 * @version 3.0.0, 03/04/2016
 * @since 3.0.0
 */
public class ServiceGenerator {
    private static final long HTTP_CACHE_SIZE = 10 * 1024 * 1024;

    private static final String HTTP_CACHE_FILE_NAME = "http";

    private static final String API_BASE_URL = "http://simapp.rmtcgoiania.com.br";

    private static Retrofit sRetrofit;

    private static Cache createCache(@NonNull Context context) {
        return new Cache(new File(context.getCacheDir(), HTTP_CACHE_FILE_NAME), HTTP_CACHE_SIZE);
    }

    private static Interceptor createLoggingInterceptor() {
        HttpLoggingInterceptor.Level level;
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.HEADERS;
        } else {
            level = HttpLoggingInterceptor.Level.NONE;
        }

        return new HttpLoggingInterceptor()
                .setLevel(level);
    }

    public static <S> S createService(@NonNull Class<S> serviceClass, @NonNull Context context) {
        if (sRetrofit == null) {
            final OkHttpClient httpClient = new OkHttpClient.Builder()
                    .cache(createCache(context))
                    .addNetworkInterceptor(new StethoInterceptor())
                    .addInterceptor(createLoggingInterceptor())
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            sRetrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(
                            RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .client(httpClient)
                    .build();
        }

        return sRetrofit.create(serviceClass);
    }
}
