package com.example.toaruifdamagecalculator.ui.model

import com.example.toaruifdamagecalculator.data.model.BattleUnit

data class CalcParams(
    val unit: BattleUnit,
    val atkType: String,
    val breakpoint: Boolean,
    val critical: Boolean,
    val color: String,
    val gwBonus: String,
    val atkStat: Int?,
    val skillLvl: Int?,
    val passive1: Int?,
    val passive2: Int?,
    val atkUp: Int?,
    val critUp: Int?,
    val defDown: Int?,
    val colorResDown: Int?
)
