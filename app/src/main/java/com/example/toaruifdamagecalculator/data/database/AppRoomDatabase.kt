package com.example.toaruifdamagecalculator.data.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.data.model.DateBackup

@Database(entities = [BattleUnit::class, DateBackup::class], version = 3)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun battleUnitDao() : Dao




    companion object {

        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(context: Context): AppRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "room_database"
                ).addMigrations()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}