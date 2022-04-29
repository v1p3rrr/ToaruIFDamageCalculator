package com.example.toaruifdamagecalculator.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.data.api.UnitApiService
import com.example.toaruifdamagecalculator.data.repository.UnitRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.Dispatcher

class MainViewModel(private val unitRepository: UnitRepository) : ViewModel() {

    private val job : Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _unitsStateFlow = MutableStateFlow<List<BattleUnit>>(ArrayList())
    val unitsStateFlow = _unitsStateFlow.asStateFlow()

    fun getAllUnits() = scope.launch {
            _unitsStateFlow.value = unitRepository.getAllUnits()
        }

}