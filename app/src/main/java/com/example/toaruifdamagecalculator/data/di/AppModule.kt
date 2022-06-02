package com.example.toaruifdamagecalculator.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.toaruifdamagecalculator.data.api.UnitApiService
import com.example.toaruifdamagecalculator.data.database.AppRoomDatabase
import com.example.toaruifdamagecalculator.data.repository.UnitRepositoryImpl
import com.example.toaruifdamagecalculator.domain.repository.UnitRepository
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
object AppModule {

    private const val BASE_URL_PC = "http://192.168.1.73:8080"
    private const val BASE_URL_LAPTOP = "http://192.168.137.1:8080"
    private const val BASE_URL_HEROKU_SERVER = "https://toaru-if-spring-boot.herokuapp.com"

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
        .baseUrl(BASE_URL_HEROKU_SERVER)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideAppRoomDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(
        context.applicationContext,
        AppRoomDatabase::class.java,
        AppRoomDatabase.DB_NAME
    ).addMigrations().build()

    @Provides
    @Singleton
    fun provideUnitRepository(
        unitApiService: UnitApiService,
        db: AppRoomDatabase
    ): UnitRepository {
        return UnitRepositoryImpl(unitApiService, db)
    }

}
