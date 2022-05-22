package com.example.toaruifdamagecalculator.data.database

import androidx.room.*
import com.example.toaruifdamagecalculator.data.model.BattleUnit

@Dao
interface BattleUnitDao {

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
}