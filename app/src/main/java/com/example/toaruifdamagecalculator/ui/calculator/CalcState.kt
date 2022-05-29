package com.example.toaruifdamagecalculator.ui.calculator

import com.example.toaruifdamagecalculator.common.AttackType
import com.example.toaruifdamagecalculator.common.ColorType
import com.example.toaruifdamagecalculator.common.GwBonusType
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import javax.inject.Inject

data class CalcState(
    val unit: BattleUnit,
    val unitId: Int? = null,
    val atkType: AttackType = AttackType.NORMAL_ATTACK, //todo like that?
    val colorType: ColorType = ColorType.NORMAL,
    val gwBonusType: GwBonusType = GwBonusType.NONE,
    val breakpoint: Boolean = false,
    val critical: Boolean = false,
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
