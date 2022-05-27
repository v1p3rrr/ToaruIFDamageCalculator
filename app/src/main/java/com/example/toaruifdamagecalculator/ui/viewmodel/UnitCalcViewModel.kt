package com.example.toaruifdamagecalculator.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toaruifdamagecalculator.data.di.annotations.IoDispatcher
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.data.repository.UnitRepositoryImpl
import com.example.toaruifdamagecalculator.domain.use_cases.Calculate
import com.example.toaruifdamagecalculator.ui.model.CalcParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val calculate: Calculate
) : ViewModel() {

    private val _unitStateFlow = MutableStateFlow(
        BattleUnit(
            -1,
            "Default Character",
            "Default Card",
            null,
        )
    )
    val unitStateFlow = _unitStateFlow.asStateFlow()

    private val _errorSharedFlow = MutableSharedFlow<String>()
    val errorSharedFlow = _errorSharedFlow.asSharedFlow()

    private lateinit var calcParams: CalcParams

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

    fun onCalculate(
        unit: BattleUnit,
        atkType: String,
        breakpoint: Boolean,
        critical: Boolean,
        color: String,
        gwBonus: String,
        atkStat: Int,
        skillLvl: Int,
        passive1: Int,
        passive2: Int,
        atkUp: Int,
        critUp: Int,
        defDown: Int,
        colorResDown: Int
    ) : Int {
        calcParams = CalcParams(
            unit,
            atkType,
            breakpoint,
            critical,
            color,
            gwBonus,
            atkStat,
            skillLvl,
            passive1,
            passive2,
            atkUp,
            critUp,
            defDown,
            colorResDown
        )
        return calculate(calcParams)
    }


}