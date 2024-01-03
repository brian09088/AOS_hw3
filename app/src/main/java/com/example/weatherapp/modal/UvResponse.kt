package com.example.weatherapp.modal
import com.google.gson.annotations.SerializedName
data class UvResponse(
    @SerializedName("result") val result: UvResult? = null
)

data class UvResult(
    @SerializedName("uv") val uv: Double? = null,
    @SerializedName("uv_time") val uvTime: String? = null,
    @SerializedName("uv_max") val uvMax: Double? = null,
    @SerializedName("uv_max_time") val uvMaxTime: String? = null,
    @SerializedName("ozone") val ozone: Double? = null,
    @SerializedName("ozone_time") val ozoneTime: String? = null,
    @SerializedName("safe_exposure_time") val safeExposureTime: SafeExposureTime? = null,
    @SerializedName("sun_info") val sunInfo: SunInfo? = null
)

data class SafeExposureTime(
    @SerializedName("st1") val st1: Int? = null,
    @SerializedName("st2") val st2: Int? = null,
    @SerializedName("st3") val st3: Int? = null,
    @SerializedName("st4") val st4: Int? = null,
    @SerializedName("st5") val st5: Int? = null,
    @SerializedName("st6") val st6: Int? = null
)

data class SunInfo(
    @SerializedName("sun_times") val sunTimes: SunTimes? = null,
    @SerializedName("sun_position") val sunPosition: SunPosition? = null
)

data class SunTimes(
    @SerializedName("solarNoon") val solarNoon: String? = null,
    @SerializedName("nadir") val nadir: String? = null,
    @SerializedName("sunrise") val sunrise: String? = null,
    @SerializedName("sunset") val sunset: String? = null,
    @SerializedName("sunriseEnd") val sunriseEnd: String? = null,
    @SerializedName("sunsetStart") val sunsetStart: String? = null,
    @SerializedName("dawn") val dawn: String? = null,
    @SerializedName("dusk") val dusk: String? = null,
    @SerializedName("nauticalDawn") val nauticalDawn: String? = null,
    @SerializedName("nauticalDusk") val nauticalDusk: String? = null,
    @SerializedName("nightEnd") val nightEnd: String? = null,
    @SerializedName("night") val night: String? = null,
    @SerializedName("goldenHourEnd") val goldenHourEnd: String? = null,
    @SerializedName("goldenHour") val goldenHour: String? = null
)

data class SunPosition(
    @SerializedName("azimuth") val azimuth: Double? = null,
    @SerializedName("altitude") val altitude: Double? = null
)