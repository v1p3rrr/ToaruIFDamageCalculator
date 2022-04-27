package com.example.toaruifdamagecalculator.ui.view

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.toaruifdamagecalculator.ToaruApp
import com.example.toaruifdamagecalculator.data.api.RetrofitBuilder
import com.example.toaruifdamagecalculator.data.api.UnitApiHelper
import com.example.toaruifdamagecalculator.data.repository.UnitRepository
import com.example.toaruifdamagecalculator.databinding.ActivityMainBinding
import com.example.toaruifdamagecalculator.ui.viewmodel.MainViewModel
import com.example.toaruifdamagecalculator.ui.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(UnitApiHelper(RetrofitBuilder.unitApiService))).get(MainViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel.units.observe(this) {
            binding.ResultDamage.text = it?.get(0)?.toString()
        }


//        mainViewModel.units.observe(this) {
//            Log.e(TAG, "onCreate: ${it.joinToString()}")
//        }
        //mainViewModel.provideApi((application as ToaruApp).unitApi)


        binding.Breakpoint.setOnCheckedChangeListener { compoundButton, isChecked ->
            //binding.ResultDamage.text = mainViewModel.fetchAllUnits()[0].toString()
//            mainViewModel.fetchAllUnits( (application as ToaruApp).unitApi,
//                onSuccess = {
//                    binding.ResultDamage.text = it?.get(0).toString()
//                },
//                onError = {
//                    binding.ResultDamage.text = it
//                }
//            )
//            mainViewModel.fetchAllUnitsRx((application as ToaruApp).unitApi)
            mainViewModel.getAllUnits()

        }
    }
}