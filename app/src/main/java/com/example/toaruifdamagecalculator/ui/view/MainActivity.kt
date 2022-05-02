package com.example.toaruifdamagecalculator.ui.view

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.toaruifdamagecalculator.data.api.RetrofitBuilder
import com.example.toaruifdamagecalculator.data.api.UnitApiHelper
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.databinding.ActivityMainBinding
import com.example.toaruifdamagecalculator.ui.viewmodel.MainViewModel
import com.example.toaruifdamagecalculator.ui.viewmodel.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding

    private var unitsList: List<BattleUnit> = arrayListOf(BattleUnit(1 ,"a", "d", "e"))

    private lateinit var unitsAdapter : ArrayAdapter<BattleUnit>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(UnitApiHelper(RetrofitBuilder.unitApiService))).get(MainViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUnitsSelectorSpinner()
        collectFlows()

        binding.Breakpoint.setOnCheckedChangeListener { compoundButton, isChecked ->
            mainViewModel.getAllUnits()
        }
    }

    private fun collectFlows(){
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mainViewModel.unitsStateFlow.collectLatest {
                    unitsList = it
                    unitsAdapter.notifyDataSetChanged()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mainViewModel.errorSharedFlow.collectLatest {
                    binding.ResultDamage.text = it
                    Snackbar.make(
                        binding.root,
                        it,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun setUnitsSelectorSpinner(){
        unitsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, unitsList)
        binding.charSelectorSpinner.adapter = unitsAdapter

        binding.charSelectorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

    }
}