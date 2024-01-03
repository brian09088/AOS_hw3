package com.example.weatherapp.modal

import com.google.gson.annotations.SerializedName

data class AirQualityResponse(
    @SerializedName("coord") val coord: Coord? = null,
    @SerializedName("list") val list: List<AirQualityListItem>? = null
)


data class Coord(
    @SerializedName("lat") val lat: Double? = null,
    @SerializedName("lon") val lon: Double? = null
)

data class AirQualityListItem(
    @SerializedName("dt") val dt: Long? = null,
    @SerializedName("main") val main: Main? = null,
    @SerializedName("components") val components: Components? = null
)


data class Main(
    @SerializedName("aqi") val aqi: Int? = null,
    @SerializedName("components") val components: Components? = null
)

data class Components(
    @SerializedName("co") val co: Double? = null,
    @SerializedName("no") val no: Double? = null,
    @SerializedName("no2") val no2: Double? = null,
    @SerializedName("o3") val o3: Double? = null,
    @SerializedName("so2") val so2: Double? = null,
    @SerializedName("pm2_5") val pm25: Double? = null,
    @SerializedName("pm10") val pm10: Double? = null,
    @SerializedName("nh3") val nh3: Double? = null
)
