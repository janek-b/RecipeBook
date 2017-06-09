package com.janek.recipebook.services;

import com.janek.recipebook.Constants;
import java.io.IOException;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpoonService {

    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("X-Mashape-Key", Constants.SPOON_KEY)
                            .header("Accept", "application/json")
                            .method(original.method(), original.body()).build();
                    return chain.proceed(request);
                }
            }).build();

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(httpClient).build();

    //TODO check response header for ratelimit objects remaining;
    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}