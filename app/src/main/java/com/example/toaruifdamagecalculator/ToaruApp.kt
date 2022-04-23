package com.example.toaruifdamagecalculator

import android.app.Application
import android.util.Log
import com.example.toaruifdamagecalculator.data.api.UnitApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ToaruApp : Application() {

    lateinit var unitApi: UnitApiService

    override fun onCreate() {
        super.onCreate()
        Log.v("Application", "onCreate")
        configureRetrofit()
    }

    fun configureRetrofit(){
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.43.152:8080")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        unitApi = retrofit.create(UnitApiService::class.java)
    }

}