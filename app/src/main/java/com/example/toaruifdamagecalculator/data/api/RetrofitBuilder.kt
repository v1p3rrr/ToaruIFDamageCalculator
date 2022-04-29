package com.example.toaruifdamagecalculator.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private const val BASE_URL_PC = "http://192.168.1.68:8080"
    private const val BASE_URL_LAPTOP = "http://192.168.1.70:8080"

    private fun getRetrofit(): Retrofit{
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL_LAPTOP)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val unitApiService: UnitApiService = getRetrofit().create(UnitApiService::class.java)


}