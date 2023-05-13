package com.example.befit;

import com.example.befit.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface RetrofitInterface {
    @GET("current")
    Call<WeatherResponse> customSearch(@Query("access_key") String API_KEY,
                                       @Query("query") String location);
}


