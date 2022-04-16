package com.example.toaruifdamagecalculator.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.toaruifdamagecalculator.data.BattleUnit
import com.example.toaruifdamagecalculator.domain.api.UnitApi
import com.example.toaruifdamagecalculator.domain.repository.UnitRepository
import retrofit2.Call

class MainViewModel(

) : ViewModel() {

    var unitRepository = UnitRepository()

    fun fetchAllUnits(
        unitApi: UnitApi,
        onSuccess: (List<BattleUnit>?) -> Unit,
        onError: (String) -> Unit
    ) : List<BattleUnit>? {
        return unitRepository.getAllUnits(unitApi, onSuccess, onError)
    }


}