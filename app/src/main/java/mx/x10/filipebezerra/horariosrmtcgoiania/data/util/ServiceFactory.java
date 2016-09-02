package mx.x10.filipebezerra.horariosrmtcgoiania.data.util;

import android.content.Context;
import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.util.concurrent.TimeUnit;
import mx.x10.filipebezerra.horariosrmtcgoiania.BuildConfig;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * @author Filipe Bezerra
 */
public class ServiceFactory {
    private static final long HTTP_CACHE_SIZE = 10 * 1024 * 1024;

    private static final String HTTP_CACHE_FILE_NAME = "http";

    private static final String BASE_URL = "http://simapp.rmtcgoiania.com.br/";

    private static Retrofit sRetrofit;

    public static <S> S createService(@NonNull Context context, @NonNull Class<S> serviceClass) {
        return getRetrofit(context).create(serviceClass);
    }

    private static Retrofit getRetrofit(Context context) {
        if (sRetrofit == null) {
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(createGson()))
                    .addCallAdapterFactory(
                            RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .client(createOkHttpClient(context))
                    .build();
        }
        return sRetrofit;
    }

    private static OkHttpClient createOkHttpClient(Context context) {
        return new OkHttpClient
                .Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .cache(createCache(context))
                .addInterceptor(createLoggingInterceptor())
                .addInterceptor(createApiInterceptor())
                .build();
    }

    private static Gson createGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    private static Cache createCache(@NonNull Context context) {
        return new Cache(new File(context.getCacheDir(), HTTP_CACHE_FILE_NAME), HTTP_CACHE_SIZE);
    }

    private static Interceptor createLoggingInterceptor() {
        return new HttpLoggingInterceptor()
                .setLevel(BuildConfig.DEBUG ?
                        HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
    }

    private static Interceptor createApiInterceptor() {
        return chain -> {
            Request original = chain.request();

            final Request request = original.newBuilder()
                    .header("Accept", "applicaton/json")
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        };
    }
}
