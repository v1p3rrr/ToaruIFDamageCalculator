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

class MainViewModel(private val unitRepository: UnitRepository) : ViewModel() {

    private val job: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _unitsStateFlow = MutableStateFlow<List<BattleUnit>>(ArrayList())
    val unitsStateFlow = _unitsStateFlow.asStateFlow()

    private val _errorSharedFlow = MutableSharedFlow<String>()
    val errorSharedFlow = _errorSharedFlow.asSharedFlow()

    fun getAllUnits() = scope.launch {
        try {
            _unitsStateFlow.value = unitRepository.getAllUnits()
        } catch (e: Exception) {
            Log.e("http error", "exception caught")
            _errorSharedFlow.emit("Server error")
        }
    }

}