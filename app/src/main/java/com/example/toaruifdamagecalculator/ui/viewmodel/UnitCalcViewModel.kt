package com.example.toaruifdamagecalculator.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class UnitCalcViewModel @Inject constructor(
    private val unitRepositoryImpl: UnitRepositoryImpl,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

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
        withContext(ioDispatcher) {
            try {
                _unitStateFlow.value = unitRepositoryImpl.getUnitById(id)
            } catch (e: Exception) {
                Log.e("http error", e.printStackTrace().toString())
                _errorSharedFlow.emit("Server error")
            }
        }
    }

}