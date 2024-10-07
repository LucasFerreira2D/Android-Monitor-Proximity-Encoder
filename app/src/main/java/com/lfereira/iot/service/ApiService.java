package com.lfereira.iot.service;

import com.lfereira.iot.model.GyroResponse;
import com.lfereira.iot.model.ProximityResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("proximidade")
    Call<ProximityResponse> getProximityData();

    @GET("giro")
    Call<GyroResponse> getGyroData();


}
