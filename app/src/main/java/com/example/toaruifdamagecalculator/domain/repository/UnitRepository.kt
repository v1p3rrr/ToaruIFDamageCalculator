package com.example.toaruifdamagecalculator.domain.repository

import android.util.Log
import com.example.toaruifdamagecalculator.data.BattleUnit
import com.example.toaruifdamagecalculator.domain.api.UnitApi
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class UnitRepository {

    //lateinit var unitApi: UnitApi

    fun getAllUnitsCallback(
        unitApi: UnitApi,
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

    var compositeDisposable = CompositeDisposable()

    fun getAllUnitsRx(unitApi: UnitApi){
        compositeDisposable.add(unitApi.getAllUnitsRx()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                return@subscribe
            }, {

            }))
    }

}