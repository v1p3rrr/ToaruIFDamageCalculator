package com.example.toaruifdamagecalculator.data.repository

import android.util.Log
import com.example.toaruifdamagecalculator.data.api.UnitApiHelper
import com.example.toaruifdamagecalculator.data.database.Dao
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.data.model.DateBackup
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class UnitRepository(private val api: UnitApiHelper, private val dao: Dao) {

    suspend fun getAllUnits() = dao.getAllUnits()

    suspend fun updateUnitsFromApiToLocal() {
        val unitsFromApi : List<BattleUnit> = api.getAllUnits()

        dao.addMultipleUnits(unitsFromApi)

    }

    suspend fun updateUnitsFromApiOnceInFewDays(days: Int){

        val currentTime = Calendar.getInstance().time

        if (dao.getDate().isEmpty()){
            dao.setDate(DateBackup(currentTime.time))
        }

        val diffInMillis = abs(currentTime.time - dao.getDate()[0].timeInMillis)
        if (TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) >= days){
            updateUnitsFromApiToLocal()
            dao.updateDate(dao.getDate()[0].also { it.timeInMillis=currentTime.time } )
        }
    }

    suspend fun getUnitById(id: Long) : BattleUnit {

        return if (id!=-1L) dao.getUnitById(id.toString()) else dao.getFirstUnit()

    }

    suspend fun setUnits(unitsList: List<BattleUnit>) = dao.addMultipleUnits(unitsList)

}