package com.vpr.toaruifdamagecalculator.domain.repository

import com.vpr.toaruifdamagecalculator.data.model.BattleUnit

interface UnitRepository {

    suspend fun getAllUnits() : List<BattleUnit>?

    suspend fun updateUnitsFromApiToLocal() : Boolean

    suspend fun updateUnitsFromApiOnceInFewDays(days: Int)

    suspend fun getUnitById(id: Long) : BattleUnit?

    suspend fun setUnits(unitsList: List<BattleUnit>)


}