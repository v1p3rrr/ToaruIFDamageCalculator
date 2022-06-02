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
    private val repository: UnitRepositoryImpl,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val calculator : Calculate
) : ViewModel() {

    private val tempInitBattleUnit = BattleUnit(
        -1,
        "Default Character",
        "Default Card",
        null,
        null
    )

    var calcState = CalcState(tempInitBattleUnit)

    private val _state = MutableStateFlow(
        calcState
    )
    val state = _state.asStateFlow()

    private val _errorSharedFlow = MutableSharedFlow<String>()
    val errorSharedFlow = _errorSharedFlow.asSharedFlow()


    private var _unitFlow = MutableStateFlow(
        tempInitBattleUnit
    )
    val unitFlow = _unitFlow.asStateFlow()

    fun getUnitById(id: Long) = viewModelScope.launch {
        withContext(ioDispatcher) {
            try {
                if (repository.getUnitById(id) != null) {
                    _unitFlow.value = repository.getUnitById(id)?:tempInitBattleUnit
                    calcState = calcState.copy(unit = repository.getUnitById(id)?:tempInitBattleUnit)
                }
            } catch (e: NullPointerException) {
                Log.e("db error", e.stackTraceToString())
            } catch (e: Exception) {
                Log.e("http error", e.stackTraceToString())
                _errorSharedFlow.emit("Server error")
            }
        }
    }

    fun onAtkTypeSpinnerChange(atkType: AttackType) {
        calcState = calcState.copy(atkType = atkType)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onBreakpointCheckBoxChange(breakpoint: Boolean) {
        calcState = calcState.copy(breakpoint = breakpoint)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onCriticalCheckBoxChange(critical: Boolean) {
        calcState = calcState.copy(critical = critical)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onSpBonusCheckBoxChange(spBonus: Boolean) {
        calcState = calcState.copy(spBonus = spBonus)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onAtkStatEditChange(atkStat: Int?) {
        calcState = calcState.copy(atkStat = atkStat)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onColorTypeSpinnerChange(colorType: ColorType) {
        calcState = calcState.copy(colorType = colorType)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onGwBonusTypeSpinnerChange(gwBonusType: GwBonusType) {
        calcState = calcState.copy(gwBonusType = gwBonusType)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onSkillLevelEditChange(skillLevel: Int?) {
        calcState = calcState.copy(skillLevel = skillLevel)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onPassive1LevelEditChange(passive1Level: Int?) {
        calcState = calcState.copy(passive1Level = passive1Level)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onPassive2LevelEditChange(passive2Level: Int?) {
        calcState = calcState.copy(passive2Level = passive2Level)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onAtkUpSliderChange(atkUp: Int?) {
        calcState = calcState.copy(atkUp = atkUp)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onCritUpSliderChange(critUp: Int?) {
        calcState = calcState.copy(critUp = critUp)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onDefDownSliderChange(defDown: Int?) {
        calcState = calcState.copy(defDown = defDown)
        _state.value = calcState
        Calculate(calcState)
    }

    fun onColorResDownSliderChange(colorResDown: Int?) {
        calcState = calcState.copy(colorResDown = colorResDown)
        _state.value = calcState
        Calculate(calcState)
    }

    fun Calculate(state: CalcState) {
        calcState = calcState.copy(expectedDamage = calculator(state))
        _state.value = calcState
    }


}