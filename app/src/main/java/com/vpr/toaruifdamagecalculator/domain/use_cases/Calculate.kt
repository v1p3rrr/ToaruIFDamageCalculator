package com.vpr.toaruifdamagecalculator.domain.use_cases

import com.vpr.toaruifdamagecalculator.common.AttackType
import com.vpr.toaruifdamagecalculator.common.ColorType
import com.vpr.toaruifdamagecalculator.common.GwBonusType
import com.vpr.toaruifdamagecalculator.ui.calculator.CalcState
import javax.inject.Inject

class Calculate @Inject constructor() {

    operator fun invoke(calcState: CalcState): Int {

        calcState.unit.passive1Activation
        calcState.unit.passive1Flat
        calcState.unit.passive1Type

        calcState.unit.passive2Activation
        calcState.unit.passive2Flat
        calcState.unit.passive2Type

        var result : Double = when (calcState.atkType) {
            AttackType.NORMAL_ATTACK -> calcNormalHit(calcState)
            AttackType.SP -> calcSp(calcState)
            AttackType.SKILL -> calcSkill(calcState)
        }

        result *= if (calcState.breakpoint) 2 else 1
        result *= if (calcState.colorType == ColorType.WEAK) 1.5 else (if (calcState.colorType == ColorType.RESIST) 0.6 else 1.0)
        when (calcState.gwBonusType){
            GwBonusType.ELEVEN -> result*=4
            GwBonusType.SIX -> result*=2.5
            GwBonusType.ONE -> result*=1.5
            GwBonusType.NONE -> result*=1
        }

        return result.toInt()

    }

    private fun calcSp(calcState: CalcState): Double {
        var result: Double = calcNormalHit(calcState)
        if (result!=0.0) {
            when (calcState.unit.spAtkMultiplier) {
                1 -> result = (result + (calcState.skillLevel ?: 0) * 2) * 1.25
                2 -> result = (result + (calcState.skillLevel ?: 0) * 4) * 1.5
                3 -> result = (result + (calcState.skillLevel ?: 0) * 6) * 1.75
                4 -> result = (result + (calcState.skillLevel ?: 0) * 8) * 2.0
            }
            //calcState.unit.spBonusType special types
            if (calcState.spBonus) result *= calcState.unit.spBonusMultiplier!! //?:1
            result *= (calcState.unit.spBonusMultiplier ?: 1).toDouble()
        }
        return result
    }

    private fun calcSkill(calcState: CalcState): Double {
        var result: Double = calcNormalHit(calcState)
        if (result!=0.0) {
            when (calcState.unit.skillAtkMultiplier) {
                1 -> result = (result + (calcState.skillLevel ?: 0) * 2) * 1.25
                2 -> result = (result + (calcState.skillLevel ?: 0) * 4) * 1.5
                3 -> result = (result + (calcState.skillLevel ?: 0) * 6) * 1.75
                4 -> result = (result + (calcState.skillLevel ?: 0) * 8) * 2.0
            }
            // if calcParams.unit.spBonusType true
            result *= (calcState.unit.spBonusMultiplier ?: 1).toDouble()
        }
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

        //buff level (gl hf)
        //passive multiplier (bonus)
        //def down
        //res down
        return result
    }


}