package com.example.toaruifdamagecalculator.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.data.api.UnitApiService
import com.example.toaruifdamagecalculator.data.repository.UnitRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import okhttp3.Dispatcher

class MainViewModel(private val unitRepository: UnitRepository) : ViewModel() {

    private val job : Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    val units = MutableLiveData<List<BattleUnit>>()

    fun getAllUnits() = scope.launch {
        val unitsList : Deferred<List<BattleUnit>> = scope.async {
            unitRepository.getAllUnits()
        }
        units.postValue(unitsList.await())
    }

    fun fetchAllUnits(
        unitApi: UnitApiService,
        onSuccess: (List<BattleUnit>?) -> Unit,
        onError: (String) -> Unit
    ) : List<BattleUnit>? {
        return unitRepository.getAllUnitsCallback(unitApi, onSuccess, onError)
    }

    fun fetchAllUnitsRx(unitApi: UnitApiService){
        return unitRepository.getAllUnitsRx(unitApi)
    }

}