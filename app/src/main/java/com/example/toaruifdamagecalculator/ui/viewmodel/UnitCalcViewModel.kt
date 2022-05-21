package com.example.toaruifdamagecalculator.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.data.repository.UnitRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class UnitCalcViewModel(private val unitRepository: UnitRepository) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)

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

    fun getUnitById(id: Long) = scope.launch {
        try {
            _unitStateFlow.value = unitRepository.getUnitById(id)
        } catch (e: Exception) {
            Log.e("http error", e.printStackTrace().toString())
            _errorSharedFlow.emit("Server error")
        }
    }

}