package com.example.toaruifdamagecalculator.domain.api

import com.example.toaruifdamagecalculator.data.BattleUnit
import retrofit2.Call
import retrofit2.http.*

interface UnitApi {

    @GET("./battleunits/getallunits")
    fun getAllUnits() : Call<List<BattleUnit>>
}