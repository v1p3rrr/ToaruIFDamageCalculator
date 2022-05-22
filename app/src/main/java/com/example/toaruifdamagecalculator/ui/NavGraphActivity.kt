package com.example.toaruifdamagecalculator.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.toaruifdamagecalculator.R
import com.example.toaruifdamagecalculator.databinding.ActivityNavGraphBinding

class NavGraphActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavGraphBinding

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavGraphBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

}