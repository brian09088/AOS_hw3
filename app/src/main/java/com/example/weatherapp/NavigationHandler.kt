package com.example.weatherapp
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.weatherapp.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.weatherapp.modal.AirQualityResponse
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.weatherapp.modal.UvResponse
import com.example.weatherapp.service.LocationHelper
import org.json.JSONObject
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class NavigationHandler (private val context:Context){
    private val locationHelper: LocationHelper = LocationHelper(context)

    private val apiService1 = RetrofitInstance.api
    private val apiService2 = RetrofitInstance.uvApi
    fun showAqi(context: Context, latitude: String, longitude: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val call = apiService1.getAirPollutionData(latitude, longitude, Utils.API_KEY)
            call.enqueue(object : Callback<AirQualityResponse> {
                override fun onResponse(
                    call: Call<AirQualityResponse>,
                    response: Response<AirQualityResponse>
                ) {
                    if (response.isSuccessful) {
                        val airQualityData = response.body()
                        // 处理返回的数据，显示 Dialog
                        showAirQualityDialog(context, airQualityData)
                    } else {
                        // 处理 API 错误
                        Log.e("API Error", response.errorBody()?.string() ?: "Unknown error")
                        showErrorDialog(context, "API Error")
                    }
                }

                override fun onFailure(call: Call<AirQualityResponse>, t: Throwable) {
                    // 处理网络错误
                    showErrorDialog(context, "Network Error: ${t.message}")
                }
            })
        }
    }

    fun showUv(context: Context, latitude: String, longitude: String) {

        GlobalScope.launch(Dispatchers.Main) {
            val call = apiService2.getUvData(latitude, longitude)

            call.enqueue(object : Callback<UvResponse> {
                override fun onResponse(
                    call: Call<UvResponse>,
                    response: Response<UvResponse>
                ) {
                    if (response.isSuccessful) {
                        val fireWeatherData = response.body()
                        showUvDialog(context, fireWeatherData)
                    } else {
                        Log.e("API Error", response.errorBody()?.string() ?: "Unknown error")
                        showErrorDialog(context, "API Error")
                    }
                }

                override fun onFailure(call: Call<UvResponse>, t: Throwable) {
                    Log.e("Network Error", t.message ?: "Unknown error")
                    showErrorDialog(context, "Network Error")
                }
            })
        }
    }

    fun showSunset(context:Context) {
        val locationHelper = LocationHelper(context)
        if (locationHelper.isLocationPermissionGranted()) {
            // Permission is granted, request location updates
            //            val calendar = Calendar.getInstance()
            //            val year = calendar.get(Calendar.YEAR)
            //            val month = calendar.get(Calendar.MONTH) + 1 // Month is zero-based
            //            val day = calendar.get(Calendar.DAY_OF_MONTH)
            //            val date = "$year-$month-$day"
            //
            requestLocationUpdatesForSun { latitude, longitude ->
                Log.d("Log", "Try Send Request")
                requestSunriseSunsetInfo(context, latitude, longitude)


            }
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                Utils.LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    fun showMemo(context: Context) {

    }



    private fun showAirQualityDialog(context: Context, airQualityData: AirQualityResponse?) {
        // 使用 airQualityData 中的數據來建構 Dialog 内容

        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Air Quality Information")

        val dialogMessage = StringBuilder()
        airQualityData?.list?.get(0)?.main?.let { airQuality ->
            dialogMessage.append("AQI: ${airQuality.aqi}\n")
        }

        alertDialogBuilder.setMessage(dialogMessage.toString())
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }



    private fun showUvDialog(context: Context, uvData: UvResponse?) {
        // 在此处实现显示 Dialog 的逻辑
        // 使用 uvData 中的数据来构建 Dialog 内容

        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("UV Information")

        val dialogMessage = StringBuilder()
        uvData?.result?.let { result ->
            dialogMessage.append("UV Index: ${result.uv}\n")
            // 添加其他信息的显示

            // 例如，如果需要显示最大 UV Index：
            //dialogMessage.append("Max UV Index: ${result.uv_max}\n")

            // 添加其他需要显示的信息
        }

        alertDialogBuilder.setMessage(dialogMessage.toString())
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    private fun showErrorDialog(context: Context, errorMessage: String) {

        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Error")
        alertDialogBuilder.setMessage(errorMessage)
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    // -------sun -------------
    private fun requestLocationUpdatesForSun(callback: (Double, Double) -> Unit)  {
        locationHelper.requestLocationUpdates { location ->
            // Log latitude and longitude here
            val latitude = location.latitude
            val longitude = location.longitude
            Toast.makeText(context, "lat: $latitude, long: $longitude", Toast.LENGTH_SHORT).show()
            callback(latitude, longitude)
        }
    }
    @SuppressLint("NewApi")
    fun convertTimeToSystemTimezone(utcTime: String, tzId: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")         // set date string formatter
        val utcDateTime = OffsetDateTime.parse(utcTime, formatter)
        val systemZoneIdOffset = ZoneId.of(OffsetDateTime.now().offset.id).rules.getOffset(Instant.now()) // get timezone offset from system with system zoneid
        Log.d("systemZoneIdOffset", systemZoneIdOffset.toString())
        val systemDateTime = utcDateTime.withOffsetSameInstant(systemZoneIdOffset)
        Log.d("systemDateTime", systemDateTime.toString())

        return formatter.format(systemDateTime)
    }

    fun requestSunriseSunsetInfo(context: Context, latitude: Double, longitude: Double) {
        // using API with sunrise-sunset.org, first create a queue for requests
        val apiRequestQueue: RequestQueue? = Volley.newRequestQueue(context)
        // make request string,handle request result
        val apiString = "https://api.sunrise-sunset.org/json?lat=$latitude&lng=$longitude&formatted=0"

        val request = StringRequest(
            Request.Method.GET, apiString,
            { result ->
                handleSunriseSunsetResponse(context, result)
                // success
            },
            { err ->
                handleSunriseSunsetError(context, err)
                // error
            }
        )
        apiRequestQueue?.add(request) // make request
    }

    private fun handleSunriseSunsetResponse(context: Context, result: String) {
        try {
            // create JSON object from response string
            val jsonObject = JSONObject(result)
            val resultsObject = jsonObject.getJSONObject("results")
            // extract sunrise and sunset time
            val sunrise = resultsObject.getString("sunrise")
            val sunset = resultsObject.getString("sunset")
            val tzId = jsonObject.getString("tzId")
            // convert sunrise and sunset time from UTC to system timezone
            val systemSunriseTime = convertTimeToSystemTimezone(sunrise, tzId)
            val systemSunsetTime = convertTimeToSystemTimezone(sunset, tzId)

            val msg = "Sunrise: $systemSunriseTime\nSunset: $systemSunsetTime"

            // Update UI on the UI thread
            (context as Activity).runOnUiThread {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle("Sunrise/Sunset Information")
                alertDialog.setMessage(msg)
                alertDialog.setPositiveButton("Close") { dialog, _ ->
                    dialog.dismiss()
                }
                alertDialog.show()
            }
        } catch (e: JSONException) {
            Log.e("JSON Parsing Error", e.toString())
        }
    }

    private fun handleSunriseSunsetError(context: Context, error: VolleyError) {
        Log.e("Volley Error", error.toString())
        // Handle the error (e.g., show a user-friendly message)
    }

}


