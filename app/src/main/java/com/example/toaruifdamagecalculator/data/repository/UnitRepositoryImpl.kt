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
    private val api: UnitApiService, db: AppRoomDatabase //todo ??
) : UnitRepository {

    private val dao = db.dao()

    override suspend fun getAllUnits() = dao.getAllUnits()

    override suspend fun updateUnitsFromApiToLocal() {
        val unitsFromApi: List<BattleUnit> = api.getAllUnits()
        dao.addMultipleUnits(unitsFromApi)
    }

    override suspend fun updateUnitsFromApiOnceInFewDays(days: Int) {
        val currentTime = Calendar.getInstance().time

        if (dao.getDate().isEmpty()) {
            dao.setDate(DateBackup(currentTime.time))
        }

        val diffInMillis = abs(currentTime.time - dao.getDate()[0].timeInMillis)
        if (TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) >= days) {//todo fix connection exception
            updateUnitsFromApiToLocal()
            dao.updateDate(dao.getDate()[0].also { it.timeInMillis = currentTime.time })
        }
    }

    override suspend fun getUnitById(id: Long): BattleUnit {
        return if (id != -1L) dao.getUnitById(id.toString()) else dao.getFirstUnit()
    }

    override suspend fun setUnits(unitsList: List<BattleUnit>) = dao.addMultipleUnits(unitsList)

}