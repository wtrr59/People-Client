package com.peopledemo1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    private static Retrofit retrofit = null;
    private static RetrofitInterface apiService = null;
    private final static String API_URL = "http://192.249.19.251:8780/";
    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public static RetrofitInterface getApiService() {
        if(apiService == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL) //api의 baseURL
                    .addConverterFactory(GsonConverterFactory.create(gson)) // 나는 데이터를 자동으로 컨버팅할 수 있게 GsonFactory를 씀
                    .build();
            apiService = retrofit.create(RetrofitInterface.class); //실제 api Method들이선언된 Interface객체 선언
        }
        return apiService;
    }
}
