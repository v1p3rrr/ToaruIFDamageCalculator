package com.example.toaruifdamagecalculator.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.toaruifdamagecalculator.data.di.annotations.IoDispatcher
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.data.repository.UnitRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UnitSearchViewModel @Inject constructor(
    private val unitRepositoryImpl: UnitRepositoryImpl,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher) : ViewModel() {

    private val _unitsStateFlow = MutableStateFlow<List<BattleUnit>>(ArrayList())
    val unitsStateFlow = _unitsStateFlow.asStateFlow()

    private val _errorSharedFlow = MutableSharedFlow<String>()
    val errorSharedFlow = _errorSharedFlow.asSharedFlow()

    fun getAllUnits() = viewModelScope.launch {
        withContext(ioDispatcher){
            try {
                _unitsStateFlow.value = unitRepositoryImpl.getAllUnits()?: arrayListOf()
                unitRepositoryImpl.updateUnitsFromApiOnceInFewDays(0) //todo change on release
            } catch (e: Exception) {
                Log.e("http error", e.stackTraceToString())
                _errorSharedFlow.emit("Connection error")
            } finally {
                _unitsStateFlow.value = unitRepositoryImpl.getAllUnits()?: arrayListOf()
            }
        }
    }

    //todo obviously fix passing layout as argument
    // how to not let coroutine job be cancelled after navigating to another screen
    // so all files could be downloaded successfully?
    fun onRefreshUpdateUnitsFromApiToDb(refreshLayout: SwipeRefreshLayout) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            try {
                unitRepositoryImpl.updateUnitsFromApiToLocal()
                _unitsStateFlow.value = unitRepositoryImpl.getAllUnits()?: arrayListOf()
            } catch (e: Exception) {
                Log.e("http error", e.stackTraceToString())
                _errorSharedFlow.emit("Connection error")
            } finally {
                refreshLayout.isRefreshing = false
            }
        }
    }
}