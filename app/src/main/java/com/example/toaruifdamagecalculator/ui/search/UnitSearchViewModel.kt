package com.example.toaruifdamagecalculator.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toaruifdamagecalculator.data.di.annotations.IoDispatcher
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.data.repository.UnitRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class UnitSearchViewModel @Inject constructor(
    private val repository: UnitRepositoryImpl,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher) : ViewModel() {

    private val _errorSharedFlow = MutableSharedFlow<String>()
    val errorSharedFlow = _errorSharedFlow.asSharedFlow()

    private val _searchState = MutableStateFlow(
        ""
    )

    val state = _searchState.asStateFlow()

    private val _unitsStateFlow = MutableStateFlow<List<BattleUnit>>(ArrayList())
    val unitsStateFlow = _unitsStateFlow.asStateFlow()



    fun getAllUnits() = viewModelScope.launch {
        withContext(ioDispatcher){
            try {
                _unitsStateFlow.value = repository.getAllUnits()?: arrayListOf()
                repository.updateUnitsFromApiOnceInFewDays(2)
            } catch (e: Exception) {
                Log.e("http error", e.stackTraceToString())
                _errorSharedFlow.emit("Connection error")
            } finally {
                _unitsStateFlow.value = repository.getAllUnits()?: arrayListOf()
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchState.value = query
    }

    fun onRefreshUpdateUnitsFromApiToDb(onRefreshCompleted: () -> Unit) =
        viewModelScope.launch {
        withContext(Dispatchers.IO){
            try {
                repository.updateUnitsFromApiToLocal()
                _unitsStateFlow.value = repository.getAllUnits()?: arrayListOf()
            } catch (e: Exception) {
                Log.e("http error", e.stackTraceToString())
                _errorSharedFlow.emit("Connection error")
            } finally {
                onRefreshCompleted()
            }
        }
    }
}