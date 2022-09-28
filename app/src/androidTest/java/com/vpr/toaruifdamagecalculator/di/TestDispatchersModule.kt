package com.vpr.toaruifdamagecalculator.di

import com.vpr.toaruifdamagecalculator.di.annotations.DefaultDispatcher
import com.vpr.toaruifdamagecalculator.di.annotations.IoDispatcher
import com.vpr.toaruifdamagecalculator.di.annotations.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
@Module
@InstallIn(SingletonComponent::class)
object TestDispatchersModule {

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher() : CoroutineDispatcher = StandardTestDispatcher()

    @Provides
    @IoDispatcher
    fun provideIoDispatcher() : CoroutineDispatcher = StandardTestDispatcher()

    @Provides
    @MainDispatcher
    fun provideMainDispatcher() : CoroutineDispatcher = StandardTestDispatcher()

}