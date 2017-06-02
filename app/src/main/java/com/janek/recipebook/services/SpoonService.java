package com.janek.recipebook.services;

import com.google.gson.reflect.TypeToken;
import com.janek.recipebook.Constants;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpoonService {

  private static OkHttpClient httpClient = new OkHttpClient.Builder()
      .addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
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
      .client(httpClient).build();

  public static <S> S createService(Class<S> serviceClass) {
    return retrofit.create(serviceClass);
  }
}