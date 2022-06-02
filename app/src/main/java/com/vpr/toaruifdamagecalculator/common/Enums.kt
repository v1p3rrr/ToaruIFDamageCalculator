package com.vpr.toaruifdamagecalculator.common

enum class AttackType (val type: String) {
    NORMAL_ATTACK("Normal attack"), SP("SP"), SKILL("Skill");

    override fun toString(): String {
        return type
    }

    companion object {
        private val map = AttackType.values().associateBy(AttackType::type)
        fun fromString(type: String) = map[type]
    }
}

enum class ColorType (val type: String) {
    NORMAL("Normal"), WEAK("Weak"), RESIST("Resist");

    override fun toString(): String {
        return type
    }

    companion object {
        private val map = ColorType.values().associateBy(ColorType::type)
        fun fromString(type: String) = map[type]
    }
}

enum class GwBonusType (val type: String) {
    NONE("None"), ONE("1+/12"), SIX("6+/12"), ELEVEN("11+/12");

    override fun toString(): String {
        return type
    }

    companion object {
        private val map = GwBonusType.values().associateBy(GwBonusType::type)
        fun fromString(type: String) = map[type]
    }
}
