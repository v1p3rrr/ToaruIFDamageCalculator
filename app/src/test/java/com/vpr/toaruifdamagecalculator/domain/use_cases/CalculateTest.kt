package com.vpr.toaruifdamagecalculator.domain.use_cases

import com.google.common.truth.Truth.assertThat
import com.vpr.toaruifdamagecalculator.common.AttackType
import com.vpr.toaruifdamagecalculator.common.ColorType
import com.vpr.toaruifdamagecalculator.common.GwBonusType
import com.vpr.toaruifdamagecalculator.data.model.BattleUnit
import com.vpr.toaruifdamagecalculator.ui.calculator.CalcState
import org.junit.Before
import org.junit.Test

class CalculateTest {

    private val battleUnitMinMultipliers = BattleUnit(
        -1,
        charName = "Default Character",
        cardName = "Default Card",
        imageUrl = null,
        imageSecondUrl = null,
        skillAtkMultiplier = 1,
        spAtkMultiplier = 1,
        spBonusMultiplier = 1F
    )

    private val battleUnitMultipliersOf2 = BattleUnit(
        -1,
        charName = "Default Character",
        cardName = "Default Card",
        imageUrl = null,
        imageSecondUrl = null,
        skillAtkMultiplier = 2,
        spAtkMultiplier = 2,
    )

    private val battleUnitMultipliersOf3 = BattleUnit(
        -1,
        charName = "Default Character",
        cardName = "Default Card",
        imageUrl = null,
        imageSecondUrl = null,
        skillAtkMultiplier = 3,
        spAtkMultiplier = 3
    )

    private val battleUnitMaxMultipliers = BattleUnit(
        -1,
        charName = "Default Character",
        cardName = "Default Card",
        imageUrl = null,
        imageSecondUrl = null,
        skillAtkMultiplier = 3,
        spAtkMultiplier = 4
    )

    private val battleUnitMaxMultipliersAnd12SpBonus = BattleUnit(
        -1,
        charName = "Default Character",
        cardName = "Default Card",
        imageUrl = null,
        imageSecondUrl = null,
        skillAtkMultiplier = 3,
        spAtkMultiplier = 4,
        spBonusMultiplier = 1.2F
    )


    lateinit var calculator: Calculate

    @Before
    fun setUp() {
        calculator = Calculate()
    }

    @Test
    fun `calc state by default returns 0`() {
        val calcState = CalcState(battleUnitMinMultipliers)
        val result = calculator(calcState)
        assertThat(result).isEqualTo(0)
    }

    @Test
    fun `normal hit returns 1000`() {
        val calcState = CalcState(
            unit = battleUnitMinMultipliers,
            atkType = AttackType.NORMAL_ATTACK,
            atkStat = 1000
        )
        val result = calculator(calcState)
        assertThat(result).isEqualTo(1000)
    }

    @Test
    fun `normal hit with weak color returns 1500`() {
        val calcState = CalcState(
            unit = battleUnitMinMultipliers,
            atkType = AttackType.NORMAL_ATTACK,
            atkStat = 1000,
            colorType = ColorType.WEAK
        )
        val result = calculator(calcState)
        assertThat(result).isEqualTo(1500)
    }

    @Test
    fun `normal hit with resist returns 600`() {
        val calcState = CalcState(
            unit = battleUnitMinMultipliers,
            atkType = AttackType.NORMAL_ATTACK,
            atkStat = 1000,
            colorType = ColorType.RESIST
        )
        val result = calculator(calcState)
        assertThat(result).isEqualTo(600)
    }

    @Test
    fun `normal hit with max gw bonus returns 4000`() {
        val calcState = CalcState(
            unit = battleUnitMinMultipliers,
            atkType = AttackType.NORMAL_ATTACK,
            atkStat = 1000,
            gwBonusType = GwBonusType.ELEVEN
        )
        val result = calculator(calcState)
        assertThat(result).isEqualTo(4000)
    }

    @Test
    fun `normal hit with max gw bonus and weak color returns 6000`() {
        val calcState = CalcState(
            unit = battleUnitMinMultipliers,
            atkType = AttackType.NORMAL_ATTACK,
            atkStat = 1000,
            gwBonusType = GwBonusType.ELEVEN,
            colorType = ColorType.WEAK
        )
        val result = calculator(calcState)
        assertThat(result).isEqualTo(6000)
    }

    @Test
    fun `normal hit with critical returns 1500`() {
        val calcState = CalcState(
            unit = battleUnitMinMultipliers,
            atkType = AttackType.NORMAL_ATTACK,
            atkStat = 1000,
            critical = true
        )
        val result = calculator(calcState)
        assertThat(result).isEqualTo(1500)
    }

    @Test
    fun `normal hit with breakpoint returns 2000`() {
        val calcState = CalcState(
            unit = battleUnitMinMultipliers,
            atkType = AttackType.NORMAL_ATTACK,
            atkStat = 1000,
            breakpoint = true
        )
        val result = calculator(calcState)
        assertThat(result).isEqualTo(2000)
    }

    @Test
    fun `normal hit with 50 atk up returns 1500`() {
        val calcState = CalcState(
            unit = battleUnitMinMultipliers,
            atkType = AttackType.NORMAL_ATTACK,
            atkStat = 1000,
            atkUp = 50
        )
        val result = calculator(calcState)
        assertThat(result).isEqualTo(1500)
    }

    @Test
    fun `normal hit with critical and 50 crit up returns 2250`() {
        val calcState = CalcState(
            unit = battleUnitMinMultipliers,
            atkType = AttackType.NORMAL_ATTACK,
            atkStat = 1000,
            critical = true,
            critUp = 50
        )
        val result = calculator(calcState)
        assertThat(result).isEqualTo(2250)
    }

    @Test
    fun `normal hit without critical and 50 crit up returns 1000`() {
        val calcState = CalcState(
            unit = battleUnitMinMultipliers,
            atkType = AttackType.NORMAL_ATTACK,
            atkStat = 1000
        )
        val result = calculator(calcState)
        assertThat(result).isEqualTo(1000)
    }

    @Test
    fun `small skill returns 800`() {
        val calcState = CalcState(
            unit = battleUnitMinMultipliers,
            atkType = AttackType.SKILL,
            atkStat = 1000
        )
        val result = calculator(calcState)
        assertThat(result).isEqualTo(800)
    }

    @Test
    fun `small skill lvl 20 returns 832`() {
        val calcState = CalcState(
            unit = battleUnitMinMultipliers,
            atkType = AttackType.SKILL,
            atkStat = 1000,
            skillLevel = 20
        )
        val result = calculator(calcState)
        assertThat(result).isEqualTo(832)
    }

    @Test
    fun `medium skill lvl 20 returns 1080`() {
        val calcState = CalcState(
            unit = battleUnitMultipliersOf2,
            atkType = AttackType.SKILL,
            atkStat = 1000,
            skillLevel = 20
        )
        val result = calculator(calcState)
        assertThat(result).isEqualTo(1080)
    }

    @Test
    fun `large skill lvl 20 returns 1224`() {
        val calcState = CalcState(
            unit = battleUnitMultipliersOf3,
            atkType = AttackType.SKILL,
            atkStat = 1000,
            skillLevel = 20
        )
        val result = calculator(calcState)
        assertThat(result).isEqualTo(1344)
    }

    @Test
    fun `small sp returns 1250`() {
        val calcState = CalcState(
            unit = battleUnitMinMultipliers,
            atkType = AttackType.SP,
            atkStat = 1000
        )
        val result = calculator(calcState)
        assertThat(result).isEqualTo(1250)
    }

    @Test
    fun `small sp lvl 20 returns 1300`() {
        val calcState = CalcState(
            unit = battleUnitMinMultipliers,
            atkType = AttackType.SP,
            atkStat = 1000,
            skillLevel = 20
        )
        val result = calculator(calcState)
        assertThat(result).isEqualTo(1300)
    }

    @Test
    fun `medium sp lvl 20 returns 1620`() {
        val calcState = CalcState(
            unit = battleUnitMultipliersOf2,
            atkType = AttackType.SP,
            atkStat = 1000,
            skillLevel = 20
        )
        val result = calculator(calcState)
        assertThat(result).isEqualTo(1620)
    }

    @Test
    fun `large sp lvl 20 returns 1960`() {
        val calcState = CalcState(
            unit = battleUnitMultipliersOf3,
            atkType = AttackType.SP,
            atkStat = 1000,
            skillLevel = 20
        )
        val result = calculator(calcState)
        assertThat(result).isEqualTo(1960)
    }

    @Test
    fun `massive sp lvl 20 returns 2320`() {
        val calcState = CalcState(
            unit = battleUnitMaxMultipliers,
            atkType = AttackType.SP,
            atkStat = 1000,
            skillLevel = 20
        )
        val result = calculator(calcState)
        assertThat(result).isEqualTo(2320)
    }

    @Test
    fun `massive sp lvl 20 max bonus returns 4042`() { //verify bonus order?
        val calcState = CalcState(
            unit = battleUnitMaxMultipliersAnd12SpBonus,
            atkType = AttackType.SP,
            atkStat = 1000,
            skillLevel = 20,
            spBonus = true
        )
        val result = calculator(calcState)
        assertThat(result).isEqualTo(3340)
    }

    @Test
    fun `massive sp lvl 20 with all max buffs and conditions returns 329529`() { //verify?
        val calcState = CalcState(
            unit = battleUnitMaxMultipliersAnd12SpBonus,
            atkType = AttackType.SP,
            atkStat = 1000,
            skillLevel = 20,
            spBonus = true,
            breakpoint = true,
            critical = true,
            colorType = ColorType.WEAK,
            gwBonusType = GwBonusType.ELEVEN,
            atkUp = 150,
            critUp = 150
        )
        val result = calculator(calcState)
        assertThat(result).isEqualTo(329529)
    }
}