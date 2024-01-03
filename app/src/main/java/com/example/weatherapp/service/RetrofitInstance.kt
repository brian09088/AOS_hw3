package com.example.weatherapp.service

import com.example.weatherapp.Utils
import com.example.weatherapp.Utils.Companion.BASE_URL
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


class RetrofitInstance {

    companion object {

        private  val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client1 = OkHttpClient.Builder().addInterceptor(logging).build()
        private val client2 = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader("x-access-token", "openuv-1eouwkrlqkvazwf-io")
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(logging)
            .build()
        //private val retrofit by lazy {
            // to log responses of retrofit
            //val logging = HttpLoggingInterceptor()
            //logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            //val client = OkHttpClient.Builder().addInterceptor(logging).build()
            //Retrofit.Builder().baseUrl(Utils.BASE_URL)
            //    .addConverterFactory(GsonConverterFactory.create()).client(client).build()

        //}

        // we will use this to make api calls
        private val retrofit by lazy {
            Retrofit.Builder().baseUrl(Utils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).client(client1).build()
        }

        // 這裡新增了兩個 Service 實例
        val api by lazy {
            retrofit.create(Service::class.java)
        }

        val uvApi by lazy{
            Retrofit.Builder().baseUrl(Utils.UV_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).client(client2).build()
                .create(Service::class.java)
        }


    }
}