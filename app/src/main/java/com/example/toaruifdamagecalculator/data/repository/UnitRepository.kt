package com.example.toaruifdamagecalculator.data.repository

import com.example.toaruifdamagecalculator.data.api.UnitApiHelper

class UnitRepository(private val unitApiHelper: UnitApiHelper) {

    suspend fun getAllUnits() = unitApiHelper.getAllUnits()

    suspend fun getUnitById(id: Long) = unitApiHelper.getUnitById(id.toString())


}