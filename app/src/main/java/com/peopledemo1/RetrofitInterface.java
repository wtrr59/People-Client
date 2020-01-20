package com.peopledemo1;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @FormUrlEncoded
    @POST("users/login")
    Call<String> sendLogin(@Field("id")String id, @Field("password")String pw);

    @POST("users/sign")
    Call<String> sendSign(@Body User user);
}
