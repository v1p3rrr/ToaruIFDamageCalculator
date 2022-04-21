package com.example.toaruifdamagecalculator.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.toaruifdamagecalculator.data.BattleUnit
import com.example.toaruifdamagecalculator.domain.api.UnitApi
import com.example.toaruifdamagecalculator.domain.repository.UnitRepository
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Call

class MainViewModel() : ViewModel() {

    private var compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    var unitRepository = UnitRepository()

    fun fetchAllUnits(
        unitApi: UnitApi,
        onSuccess: (List<BattleUnit>?) -> Unit,
        onError: (String) -> Unit
    ) : List<BattleUnit>? {
        return unitRepository.getAllUnitsCallback(unitApi, onSuccess, onError)
    }

    fun fetchAllUnitsRx(unitApi: UnitApi){
        return unitRepository.getAllUnitsRx(unitApi)
    }

}