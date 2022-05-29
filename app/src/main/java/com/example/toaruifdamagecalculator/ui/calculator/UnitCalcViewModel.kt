package com.example.toaruifdamagecalculator.ui.calculator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toaruifdamagecalculator.common.AttackType
import com.example.toaruifdamagecalculator.common.ColorType
import com.example.toaruifdamagecalculator.common.GwBonusType
import com.example.toaruifdamagecalculator.data.di.annotations.IoDispatcher
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.data.repository.UnitRepositoryImpl
import com.example.toaruifdamagecalculator.domain.use_cases.Calculate
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
) : ViewModel() {

    private val tempInitBattleUnit = BattleUnit(
        -1,
        "Default Character",
        "Default Card",
        null,
    )

    val calculator = Calculate()

    private val _unitFlow = MutableStateFlow(
        tempInitBattleUnit
    )
    val unitFlow = _unitFlow.asStateFlow()

    var calcState = CalcState(tempInitBattleUnit)

    private val _state = MutableStateFlow(
        CalcState(tempInitBattleUnit)
    )
    val state = _state.asStateFlow()

    private val _errorSharedFlow = MutableSharedFlow<String>()
    val errorSharedFlow = _errorSharedFlow.asSharedFlow()


    fun getUnitById(id: Long) = viewModelScope.launch {
        withContext(ioDispatcher) {
            try {
                if (unitRepositoryImpl.getUnitById(id) != null) {
                    _unitFlow.value = unitRepositoryImpl.getUnitById(id)?:tempInitBattleUnit
                    calcState = calcState.copy(unit = unitRepositoryImpl.getUnitById(id)?:tempInitBattleUnit)
                }
            } catch (e: NullPointerException) {
                Log.e("db error", e.stackTraceToString())
            } catch (e: Exception) {
                Log.e("http error", e.stackTraceToString())
                _errorSharedFlow.emit("Server error")
            }
        }
    }

    fun onAtkTypeSpinnerChangeEvent(atkType: AttackType) {
        calcState = calcState.copy(atkType = atkType)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onBreakpointCheckBoxChangeEvent(breakpoint: Boolean) {
        calcState = calcState.copy(breakpoint = breakpoint)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onCriticalCheckBoxChangeEvent(critical: Boolean) {
        calcState = calcState.copy(critical = critical)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onAtkStatEditChangeEvent(atkStat: Int?) {
        calcState = calcState.copy(atkStat = atkStat)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onColorTypeSpinnerChangeEvent(colorType: ColorType) {
        calcState = calcState.copy(colorType = colorType)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onGwBonusTypeSpinnerChangeEvent(gwBonusType: GwBonusType) {
        calcState = calcState.copy(gwBonusType = gwBonusType)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onSkillLevelEditChangeEvent(skillLevel: Int?) {
        calcState = calcState.copy(skillLevel = skillLevel)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onPassive1LevelEditChangeEvent(passive1Level: Int?) {
        calcState = calcState.copy(passive1Level = passive1Level)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onPassive2LevelEditChangeEvent(passive2Level: Int?) {
        calcState = calcState.copy(passive2Level = passive2Level)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onAtkUpEditChangeEvent(atkUp: Int?) {
        calcState = calcState.copy(atkUp = atkUp)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onCritUpEditChangeEvent(critUp: Int?) {
        calcState = calcState.copy(critUp = critUp)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onDefDownEditChangeEvent(defDown: Int?) {
        calcState = calcState.copy(defDown = defDown)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onColorResDownEditChangeEvent(colorResDown: Int?) {
        calcState = calcState.copy(colorResDown = colorResDown)
        _state.value = calcState
        Calculate(calcState)
    }

    fun Calculate(state: CalcState) {
        calcState = calcState.copy(expectedDamage = calculator(state))
        _state.value = calcState
    }


}