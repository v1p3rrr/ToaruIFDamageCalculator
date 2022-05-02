package com.example.toaruifdamagecalculator.data.api

import com.example.toaruifdamagecalculator.data.model.BattleUnit
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface UnitApiService {

    @GET("./battleunits/getallunits")
    fun getAllUnitsCallback() : Call<List<BattleUnit>>

    @GET("./battleunits/getallunits")
    fun getAllUnitsRx() : Single<List<BattleUnit>>

    @GET("./battleunits/getallunits")
    suspend fun getAllUnits() : ArrayList<BattleUnit>
}