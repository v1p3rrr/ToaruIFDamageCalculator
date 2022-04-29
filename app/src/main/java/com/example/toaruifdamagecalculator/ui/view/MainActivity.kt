package com.example.toaruifdamagecalculator.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.toaruifdamagecalculator.data.api.RetrofitBuilder
import com.example.toaruifdamagecalculator.data.api.UnitApiHelper
import com.example.toaruifdamagecalculator.databinding.ActivityMainBinding
import com.example.toaruifdamagecalculator.ui.viewmodel.MainViewModel
import com.example.toaruifdamagecalculator.ui.viewmodel.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(UnitApiHelper(RetrofitBuilder.unitApiService))).get(MainViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        collectFlows()

        binding.Breakpoint.setOnCheckedChangeListener { compoundButton, isChecked ->
            mainViewModel.getAllUnits()
        }
    }

    private fun collectFlows(){
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mainViewModel.unitsStateFlow.collectLatest {
                    binding.ResultDamage.text = it.joinToString()
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
}