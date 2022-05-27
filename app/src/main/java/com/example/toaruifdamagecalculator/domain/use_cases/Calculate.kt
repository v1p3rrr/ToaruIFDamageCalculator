package com.example.toaruifdamagecalculator.domain.use_cases

import com.example.toaruifdamagecalculator.data.di.annotations.IoDispatcher
import com.example.toaruifdamagecalculator.ui.model.CalcParams
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class Calculate @Inject constructor(
    //@IoDispatcher private val ioDispatcher: CoroutineDispatcher,
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
            "Normal attack" -> result = calcNormalHit(calcParams).toInt()
            "SP" -> result = calcSp(calcParams).toInt()
            "Skill" -> result = calcSkill(calcParams).toInt()
        }

        return result

    }

    private fun calcSp(calcParams: CalcParams): Double {
        var result: Double = calcNormalHit(calcParams)
        when (calcParams.unit.spAtkMultiplier) {
            1 -> result = (result + (calcParams.skillLvl?:1)*2) * 1.25.toInt()
            2 -> result = (result + (calcParams.skillLvl?:1)*4) * 1.5.toInt()
            3 -> result = (result + (calcParams.skillLvl?:1)*6) * 1.75.toInt()
            4 -> result = (result + (calcParams.skillLvl?:1)*8) * 2
        }
        // if calcParams.unit.spBonusType true
        result *= (calcParams.unit.spBonusMultiplier ?: 1)
        return result
    }

    private fun calcSkill(calcParams: CalcParams): Double {
        var result: Double = calcNormalHit(calcParams)
        when (calcParams.unit.skillAtkMultiplier) {
            1 -> result = (result + (calcParams.skillLvl?:1)*2) * 1.25
            2 -> result = (result + (calcParams.skillLvl?:1)*4) * 1.5
            3 -> result = (result + (calcParams.skillLvl?:1)*6) * 1.75
            4 -> result = (result + (calcParams.skillLvl?:1)*8) * 2
        }
        // if calcParams.unit.spBonusType true
        result *= (calcParams.unit.spBonusMultiplier ?: 1)
        return result
    }

    private fun calcNormalHit(calcParams: CalcParams): Double {
        var result: Double = calcParams.atkStat?.toDouble()?:0.0
        //battle + assist stats
        //matching color bonus
        //aura+passives
        result *= (1 + (calcParams.atkUp?:0).toDouble() / 100)

        if (calcParams.critical)
            result *= (1 + ((calcParams.critUp?:0).toDouble() / 100)) * 1.5
        result *= if (calcParams.breakpoint) 2 else 1
        //buff level (gl hf)
        //passive multiplier (bonus)
        //def down
        //res down
        return result
    }


}