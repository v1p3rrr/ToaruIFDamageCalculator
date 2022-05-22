package com.example.toaruifdamagecalculator.data.repository

import com.example.toaruifdamagecalculator.data.api.UnitApiHelper
import com.example.toaruifdamagecalculator.data.database.BattleUnitDao
import com.example.toaruifdamagecalculator.data.model.BattleUnit

class UnitRepository(private val api: UnitApiHelper, private val dao: BattleUnitDao) {

    //todo how to define if repo has to use room or api?
    suspend fun getAllUnits() = dao.getAllUnits()

    suspend fun updateUnitsFromApiToLocal() : List<BattleUnit> {
        val unitsFromApi : List<BattleUnit> = api.getAllUnits()

        dao.addMultipleUnits(unitsFromApi)

        return dao.getAllUnits()
    }

    suspend fun getUnitById(id: Long) : BattleUnit {

        return if (id!=-1L) dao.getUnitById(id.toString()) else dao.getFirstUnit()

    }

    suspend fun setUnits(unitsList: List<BattleUnit>) = dao.addMultipleUnits(unitsList)

}