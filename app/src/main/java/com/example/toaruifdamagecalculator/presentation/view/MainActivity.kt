package com.example.toaruifdamagecalculator.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.toaruifdamagecalculator.R
import com.example.toaruifdamagecalculator.ToaruApp
import com.example.toaruifdamagecalculator.data.BattleUnit
import com.example.toaruifdamagecalculator.databinding.ActivityMainBinding
import com.example.toaruifdamagecalculator.presentation.viewmodel.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val vmProvider = ViewModelProvider(this)
        mainViewModel = vmProvider.get(MainViewModel::class.java)
        //mainViewModel.provideApi((application as ToaruApp).unitApi)


        binding.Breakpoint.setOnCheckedChangeListener { compoundButton, isChecked ->
            //binding.ResultDamage.text = mainViewModel.fetchAllUnits()[0].toString()
            mainViewModel.fetchAllUnits( (application as ToaruApp).unitApi,
                onSuccess = {
                    binding.ResultDamage.text = it?.get(0).toString()
                },
                onError = {
                    binding.ResultDamage.text = it
                }
            )
            mainViewModel.fetchAllUnitsRx((application as ToaruApp).unitApi)
        }
    }
}