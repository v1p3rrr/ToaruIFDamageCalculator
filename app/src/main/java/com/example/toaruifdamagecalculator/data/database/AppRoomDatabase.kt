package com.example.toaruifdamagecalculator.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.toaruifdamagecalculator.data.model.BattleUnit

@Database(entities = [BattleUnit::class], version = 1)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun battleUnitDao() : BattleUnitDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(context: Context): AppRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "room_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}