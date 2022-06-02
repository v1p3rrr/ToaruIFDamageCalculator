package com.example.toaruifdamagecalculator.data.repository

import com.example.toaruifdamagecalculator.data.api.UnitApiService
import com.example.toaruifdamagecalculator.data.database.AppRoomDatabase
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.data.model.DateBackup
import com.example.toaruifdamagecalculator.domain.repository.UnitRepository
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

@Singleton
class UnitRepositoryImpl @Inject constructor(
    private val api: UnitApiService,
    db: AppRoomDatabase
) : UnitRepository {

    private val dao = db.dao()

    override suspend fun getAllUnits() = dao.getAllUnits()?.sortedWith( compareBy({it.charName}, {it.cardName}))

    //todo set different response for different exception types
    // also maybe update last upd date after refreshing manually?
    override suspend fun updateUnitsFromApiToLocal() : Boolean{
        val unitsFromApi: List<BattleUnit> = api.getAllUnits()
        try {
            dao.clearDb()
            dao.addMultipleUnits(unitsFromApi)
            return true
        } catch (e: Exception){
            e.printStackTrace()
            return false
        }
    }

    override suspend fun updateUnitsFromApiOnceInFewDays(days: Int) {
        val currentTime = Calendar.getInstance().time

        val lastSavedDate = dao.getDate()

        val diffInMillis: Long = if (lastSavedDate.isNullOrEmpty()) {
            currentTime.time
        } else {
            abs(currentTime.time - lastSavedDate[0].timeInMillis)
        }

        if (TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) > days) {
            if (updateUnitsFromApiToLocal()) {
                if (lastSavedDate.isNullOrEmpty()) dao.setDate(DateBackup(currentTime.time))
                else dao.updateDate(lastSavedDate[0].run { this.timeInMillis = currentTime.time; this })
            }
        }
    }

    override suspend fun getUnitById(id: Long): BattleUnit? {
        return if (id != -1L) dao.getUnitById(id.toString()) else dao.getFirstUnit()
    }

    override suspend fun setUnits(unitsList: List<BattleUnit>) = dao.addMultipleUnits(unitsList)

}