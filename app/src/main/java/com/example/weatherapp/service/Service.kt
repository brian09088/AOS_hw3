package com.example.weatherapp.service

import com.example.weatherapp.ForeCast
import com.example.weatherapp.Utils
import com.example.weatherapp.modal.AirQualityResponse
import com.example.weatherapp.modal.UvResponse
import com.google.android.gms.common.api.internal.ApiKey
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {


     @GET("forecast?")
       fun getCurrentWeather(
        @Query("lat")
        lat: String,
        @Query("lon")
        lon: String,
        @Query("appid")
        appid: String = Utils.API_KEY

    ): Call<ForeCast>


    @GET("forecast?")
    fun getWeatherByCity(
        @Query("q")
        city: String,
        @Query("appid")
        appid: String = Utils.API_KEY

    ): Call<ForeCast>

    @GET("air_pollution")
    fun getAirPollutionData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") apiKey: String = Utils.API_KEY,
    ): Call<AirQualityResponse>

    @GET("uv")
    fun getUvData(
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("alt") alt: String = "",  // 選擇性參數，默認為空字符串
        @Query("dt") dt: String = "",    // 選擇性參數，默認為空字符串
        @Query("key") apiKey: String = Utils.UV_API_KEY
    ): Call<UvResponse>


}