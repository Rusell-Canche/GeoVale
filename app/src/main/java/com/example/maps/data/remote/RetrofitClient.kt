package com.example.maps.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private final val BASE_URL: String = "http://10.0.2.2:8000/api/"
    // ⚠️ 10.0.2.2 apunta al localhost de tu PC desde el emulador Android

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}