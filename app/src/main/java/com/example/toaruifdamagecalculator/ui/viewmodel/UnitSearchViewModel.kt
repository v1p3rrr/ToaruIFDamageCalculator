package com.example.toaruifdamagecalculator.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.data.repository.UnitRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UnitSearchViewModel(private val unitRepository: UnitRepository) : ViewModel() {

    private val _unitsStateFlow = MutableStateFlow<List<BattleUnit>>(ArrayList())
    val unitsStateFlow = _unitsStateFlow.asStateFlow()

    private val _errorSharedFlow = MutableSharedFlow<String>()
    val errorSharedFlow = _errorSharedFlow.asSharedFlow()

    fun getAllUnits() = viewModelScope.launch {
        withContext(Dispatchers.IO){
            try {
                unitRepository.updateUnitsFromApiOnceInFewDays(1)
                _unitsStateFlow.value = unitRepository.getAllUnits()
            } catch (e: Exception) {
                Log.e("http error", e.printStackTrace().toString())
                _errorSharedFlow.emit("Server error")
            }
        }
    }

    //todo obviously fix passing layout as argument
    // how to not let coroutine job be cancelled after navigating to another screen
    // so all files could be downloaded successfully?
    fun onRefreshUpdateUnitsFromApiToDb(refreshLayout: SwipeRefreshLayout) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            try {
                unitRepository.updateUnitsFromApiToLocal()
            } catch (e: Exception) {
                Log.e("http error", e.printStackTrace().toString())
                _errorSharedFlow.emit("Server error")
            } finally {
                refreshLayout.isRefreshing = false
            }
        }
    }
}