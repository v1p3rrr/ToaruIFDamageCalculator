package com.example.toaruifdamagecalculator.data.database

import androidx.room.*
import androidx.room.Dao
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.data.model.DateBackup

@Dao
interface Dao {

    @Query("SELECT * FROM battleunit WHERE id = :id")
    suspend fun getUnitById(id: String) : BattleUnit

    @Query("SELECT * FROM battleunit WHERE id=(SELECT MAX(id) FROM battleunit)")
    suspend fun getFirstUnit() : BattleUnit

    @Query("SELECT * FROM battleunit WHERE id=(SELECT MIN(id) FROM battleunit)")
    suspend fun getLastUnit() : BattleUnit

    @Query("SELECT * FROM battleunit")
    suspend fun getAllUnits() : List<BattleUnit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUnit(unit: BattleUnit)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMultipleUnits(unitsList: List<BattleUnit>)

    @Delete
    suspend fun deleteUnit(unit: BattleUnit)

    @Update
    suspend fun updateUnit(unit: BattleUnit)



    @Query("SELECT * FROM date WHERE id=(SELECT MIN(id) FROM date)")
    suspend fun getDate() : List<DateBackup>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setDate(date: DateBackup)

    @Delete
    suspend fun deleteDate(date: DateBackup)

    @Update
    suspend fun updateDate(date: DateBackup)
}