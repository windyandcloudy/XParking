package com.example.qlguixe.ApiController;


import com.example.qlguixe.Models.Account;
import com.example.qlguixe.Models.Statistical;
import com.example.qlguixe.Models.Transport;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiController {
    String DOMAIN = "https://b49c-118-69-3-112.ap.ngrok.io/api/";
//    String DOMAIN = "https://guixedat.herokuapp.com/api/";
    Gson gson =new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setLenient().create();

    ApiController apiService = new Retrofit.Builder()
            .baseUrl(DOMAIN)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiController.class);


    @POST("user/login")
    Call<Account> login(@Body Account account);

    @GET("transport")
    Call<List<Transport>> getTransportOfUser(@Query("iduser") String idUser);

    @POST("transport")
    Call<Transport> createTransport(@Query("iduser") String idUser, @Body Transport transport);

    @GET("statistical")
    Call<List<Statistical>> getAllIn(@Query("isout") String isout);

    @POST("statistical")
    Call<Statistical> parking(@Query("trans_license") String bienSo);

    @PATCH("statistical")
    Call<Statistical> deparking(@Query("qr") String qr);

    @GET("user")
    Call<List<Account>> getAllUser();

    @PATCH("user/{iduser}")
    Call<Account> updateUserMoney(@Path("iduser") String idUser, @Body Account account);

    @POST("user")
    Call<Account> createAccount(@Body Account account);

    @DELETE("user/{iduser}")
    Call<Account> deleteAccount(@Path("iduser") String idUser);

    @DELETE("statistical/{id}")
    Call<String> deleteStatistical(@Path("id") String id);

    @PATCH("user/{iduser}")
    Call<Account> updateUserPass(@Path("iduser") String idUser, @Body Account account);
}
