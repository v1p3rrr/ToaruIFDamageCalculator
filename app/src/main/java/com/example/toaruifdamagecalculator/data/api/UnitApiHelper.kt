package com.example.toaruifdamagecalculator.data.api

class UnitApiHelper(private val unitApiService: UnitApiService) {

    suspend fun getAllUnits() = unitApiService.getAllUnits()

    suspend fun getUnitById(id: String) = unitApiService.getUnitById(id)

}