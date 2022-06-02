package com.vpr.toaruifdamagecalculator.ui.calculator

import com.vpr.toaruifdamagecalculator.common.AttackType
import com.vpr.toaruifdamagecalculator.common.ColorType
import com.vpr.toaruifdamagecalculator.common.GwBonusType
import com.vpr.toaruifdamagecalculator.data.model.BattleUnit

data class CalcState(
    val unit: BattleUnit,
    val unitId: Int? = null,
    val atkType: AttackType = AttackType.NORMAL_ATTACK,
    val colorType: ColorType = ColorType.NORMAL,
    val gwBonusType: GwBonusType = GwBonusType.NONE,
    val breakpoint: Boolean = false,
    val critical: Boolean = false,
    val spBonus: Boolean = false,
    val atkStat: Int? = 0,
    val skillLevel: Int? = 0,
    val passive1Level: Int? = 0,
    val passive2Level: Int? = 0,
    val atkUp: Int? = 0,
    val critUp: Int? = 0,
    val defDown: Int? = 0,
    val colorResDown: Int? = 0,
    val expectedDamage: Int = 0
)
