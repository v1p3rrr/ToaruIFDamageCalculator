package com.example.toaruifdamagecalculator.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.toaruifdamagecalculator.data.api.UnitApiHelper
import com.example.toaruifdamagecalculator.data.repository.UnitRepository
import java.lang.IllegalArgumentException

class MainViewModelFactory(private val unitApiHelper: UnitApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(UnitRepository(unitApiHelper)) as T
        }
        else throw IllegalArgumentException("Unknown class name")
    }

}