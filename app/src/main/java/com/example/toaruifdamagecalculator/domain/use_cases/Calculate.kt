package com.example.toaruifdamagecalculator.domain.use_cases

import com.example.toaruifdamagecalculator.common.AttackType
import com.example.toaruifdamagecalculator.data.di.annotations.IoDispatcher
import com.example.toaruifdamagecalculator.ui.calculator.CalcState
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class Calculate @Inject constructor(
    //@IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    //todo how to make injects correctly?

    operator fun invoke(calcState: CalcState): Int {

        var result = 0

        calcState.unit.passive1Activation
        calcState.unit.passive1Flat
        calcState.unit.passive1Type

        calcState.unit.passive2Activation
        calcState.unit.passive2Flat
        calcState.unit.passive2Type

        result = when (calcState.atkType) {
            AttackType.NORMAL_ATTACK -> calcNormalHit(calcState).toInt()
            AttackType.SP -> calcSp(calcState).toInt()
            AttackType.SKILL -> calcSkill(calcState).toInt()
        }

        return result

    }

    private fun calcSp(calcState: CalcState): Double {
        var result: Double = calcNormalHit(calcState)
        when (calcState.unit.spAtkMultiplier) {
            1 -> result = (result + (calcState.skillLevel?:1)*2) * 1.25
            2 -> result = (result + (calcState.skillLevel?:1)*4) * 1.5
            3 -> result = (result + (calcState.skillLevel?:1)*6) * 1.75
            4 -> result = (result + (calcState.skillLevel?:1)*8) * 2
        }
        // if calcParams.unit.spBonusType true
        result *= (calcState.unit.spBonusMultiplier ?: 1)
        return result
    }

    private fun calcSkill(calcState: CalcState): Double {
        var result: Double = calcNormalHit(calcState)
        when (calcState.unit.skillAtkMultiplier) {
            1 -> result = (result + (calcState.skillLevel?:1)*2) * 1.25
            2 -> result = (result + (calcState.skillLevel?:1)*4) * 1.5
            3 -> result = (result + (calcState.skillLevel?:1)*6) * 1.75
            4 -> result = (result + (calcState.skillLevel?:1)*8) * 2
        }
        // if calcParams.unit.spBonusType true
        result *= (calcState.unit.spBonusMultiplier ?: 1)
        return result
    }

    private fun calcNormalHit(calcState: CalcState): Double {
        var result: Double = calcState.atkStat?.toDouble()?:0.0
        //battle + assist stats
        //matching color bonus
        //aura+passives
        result *= (1 + (calcState.atkUp?:0).toDouble() / 100)

        if (calcState.critical)
            result *= (1 + ((calcState.critUp?:0).toDouble() / 100)) * 1.5
        result *= if (calcState.breakpoint) 2 else 1
        //buff level (gl hf)
        //passive multiplier (bonus)
        //def down
        //res down
        return result
    }


}