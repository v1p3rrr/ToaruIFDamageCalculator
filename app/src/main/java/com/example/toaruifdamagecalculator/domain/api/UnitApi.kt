package com.example.toaruifdamagecalculator.domain.api

import com.example.toaruifdamagecalculator.data.BattleUnit
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface UnitApi {

    @GET("./battleunits/getallunits")
    fun getAllUnitsCallback() : Call<List<BattleUnit>>

    @GET("./battleunits/getallunits")
    fun getAllUnitsRx() : Single<List<BattleUnit>>
}