package com.example.toaruifdamagecalculator.data.api

import com.example.toaruifdamagecalculator.data.model.BattleUnit
import retrofit2.http.*

interface UnitApiService {

    @GET("./battleunits/getunit")
    suspend fun getUnitById(@Query("id") id: String) : BattleUnit

    @GET("./battleunits/getallunits")
    suspend fun getAllUnits() : ArrayList<BattleUnit>
}