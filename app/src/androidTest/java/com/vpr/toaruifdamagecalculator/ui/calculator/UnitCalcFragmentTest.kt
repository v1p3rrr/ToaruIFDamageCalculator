package com.vpr.toaruifdamagecalculator.ui.calculator

import com.vpr.toaruifdamagecalculator.di.AppModule
import com.vpr.toaruifdamagecalculator.di.DispatchersModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class, DispatchersModule::class)
class UnitCalcFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    private fun setUp(){
        hiltRule.inject()
    }

    @Test
    fun doSomething(){

    }

}