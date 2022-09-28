package com.vpr.toaruifdamagecalculator.di

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.vpr.toaruifdamagecalculator.data.api.UnitApiService
import com.vpr.toaruifdamagecalculator.data.database.AppRoomDatabase
import com.vpr.toaruifdamagecalculator.data.repository.UnitRepositoryImpl
import com.vpr.toaruifdamagecalculator.domain.repository.UnitRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {


    private const val FAKE_URL = "https://nowhere.nowhere"

    @Singleton
    @Provides
    fun provideUnitApiService(retrofit: Retrofit): UnitApiService = retrofit.create()


    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(FAKE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideAppRoomDatabase(
        @ApplicationContext context: Context,
    ) = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        AppRoomDatabase::class.java,
    ).allowMainThreadQueries().build()

    @Provides
    @Singleton
    fun provideUnitRepository(
        unitApiService: UnitApiService,
        db: AppRoomDatabase
    ): UnitRepository {
        return UnitRepositoryImpl(unitApiService, db)
    }

}
