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

@Database(entities = [BattleUnit::class, DateBackup::class], version = 4)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun dao() : Dao

    companion object {
        const val DB_NAME = "room_database"
    }
}