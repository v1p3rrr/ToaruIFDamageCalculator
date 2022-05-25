package com.example.toaruifdamagecalculator.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.data.repository.UnitRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UnitCalcViewModel @Inject constructor(private val unitRepositoryImpl: UnitRepositoryImpl) : ViewModel() {

    private val _unitStateFlow = MutableStateFlow(
        BattleUnit(
            -1,
            "Default Character",
            "Default Card",
            null
        )
    )
    val unitStateFlow = _unitStateFlow.asStateFlow()

    private val _errorSharedFlow = MutableSharedFlow<String>()
    val errorSharedFlow = _errorSharedFlow.asSharedFlow()

    fun getUnitById(id: Long) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            try {
                _unitStateFlow.value = unitRepositoryImpl.getUnitById(id)
            } catch (e: Exception) {
                Log.e("http error", e.printStackTrace().toString())
                _errorSharedFlow.emit("Server error")
            }
        }
    }

}