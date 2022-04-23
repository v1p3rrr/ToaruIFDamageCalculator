package com.example.toaruifdamagecalculator.data.repository

import android.util.Log
import com.example.toaruifdamagecalculator.data.api.UnitApiHelper
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.data.api.UnitApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UnitRepository(private val unitApiHelper: UnitApiHelper) {

    //lateinit var unitApi: UnitApi

    fun getAllUnitsCallback(
        unitApi: UnitApiService,
        onSuccess: (List<BattleUnit>?) -> Unit,
        onError: (String) -> Unit
    ) : List<BattleUnit>?{
        var responseBody : List<BattleUnit>? = null
        unitApi.getAllUnitsCallback().enqueue(
            object : Callback<List<BattleUnit>> {
                override fun onResponse(
                    call: Call<List<BattleUnit>>,
                    response: Response<List<BattleUnit>>
                ) {
                    Log.v("Retrofit", response.body()?.joinToString().toString())
                    responseBody = response.body()
                    onSuccess(response.body())
                }

                override fun onFailure(call: Call<List<BattleUnit>>, t: Throwable) {
                    Log.e("Retrofit", t.localizedMessage)
                    onError(t.localizedMessage)
                }

            }
        )
        return responseBody
    }

    suspend fun getAllUnits() = unitApiHelper.getAllUnits()

    fun getAllUnitsRx(unitApi: UnitApiService){
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(unitApi.getAllUnitsRx()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                return@subscribe
            }, {

            }))
    }

}