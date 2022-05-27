package com.example.toaruifdamagecalculator.domain.use_cases

import com.example.toaruifdamagecalculator.data.di.annotations.IoDispatcher
import com.example.toaruifdamagecalculator.ui.model.CalcParams
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class Calculate @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    //todo suspend??
    operator fun invoke(calcParams: CalcParams): Int {

        var result: Int = 0

        calcParams.unit.passive1Activation
        calcParams.unit.passive1Flat
        calcParams.unit.passive1Type

        calcParams.unit.passive2Activation
        calcParams.unit.passive2Flat
        calcParams.unit.passive2Type

        when (calcParams.atkType) {
            "Normal attack" -> result = calcNormalHit(calcParams)
            "SP" -> result = calcSp(calcParams)
            "Skill" -> result = calcSkill(calcParams)
        }

        return result

    }

    private fun calcSp(calcParams: CalcParams): Int {
        var result: Int = 0
        calcParams.unit.spAtkMultiplier
        calcParams.unit.spBonusMultiplier
        calcParams.unit.spBonusType

        return result
    }

    private fun calcSkill(calcParams: CalcParams): Int {
        var result: Int = 0
        calcParams.unit.skillAtkMultiplier
        calcParams.unit.skillBonusMagic
        calcParams.unit.skillBonusScience

        return result
    }

    private fun calcNormalHit(calcParams: CalcParams): Int {
        return calcParams.atkStat * (if (calcParams.breakpoint) 2 else 1) *
                (1 + calcParams.atkUp / 100) *
                (if (calcParams.critical) (1 / calcParams.critUp / 100) * 1.5 else 1).toInt()
    }


}