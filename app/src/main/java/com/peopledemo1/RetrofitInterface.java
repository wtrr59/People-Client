package com.peopledemo1;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RetrofitInterface {
    @FormUrlEncoded
    @POST("users/login")
    Call<String> sendLogin(@Field("email")String email, @Field("password")String pw);

    @POST("users/sign")
    Call<String> sendSign(@Body User user);

    @Multipart
    @POST("/upload")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("upload") RequestBody name);

    @Multipart
    @POST("/uploadprofile")
    Call<ResponseBody> postProfileImage(@Part MultipartBody.Part image, @Part("upload") RequestBody name);

    @GET("users/user/{email}")
    Call<User> receiveUser(@Path("email")String email);

    @PUT("users/user/{email}")
    Call<User> updateUser(@Path("email")String email, @Body User user);
}
