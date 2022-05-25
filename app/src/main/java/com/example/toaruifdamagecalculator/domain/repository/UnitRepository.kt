package com.example.toaruifdamagecalculator.domain.repository

import com.example.toaruifdamagecalculator.data.model.BattleUnit

interface UnitRepository {

    suspend fun getAllUnits() : List<BattleUnit>

    suspend fun updateUnitsFromApiToLocal()

    suspend fun updateUnitsFromApiOnceInFewDays(days: Int)

    suspend fun getUnitById(id: Long) : BattleUnit

    suspend fun setUnits(unitsList: List<BattleUnit>)


}