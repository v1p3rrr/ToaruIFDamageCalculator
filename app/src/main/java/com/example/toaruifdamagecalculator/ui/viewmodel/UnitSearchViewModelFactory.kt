package com.example.toaruifdamagecalculator.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.toaruifdamagecalculator.data.api.UnitApiHelper
import com.example.toaruifdamagecalculator.data.database.AppRoomDatabase
import com.example.toaruifdamagecalculator.data.database.BattleUnitDao
import com.example.toaruifdamagecalculator.data.repository.UnitRepository
import java.lang.IllegalArgumentException

class UnitSearchViewModelFactory(private val unitApiHelper: UnitApiHelper, private val unitDao: BattleUnitDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UnitSearchViewModel::class.java)) {
            return UnitSearchViewModel(UnitRepository(unitApiHelper, unitDao)) as T
        }
        else throw IllegalArgumentException("Unknown class name")
    }

}