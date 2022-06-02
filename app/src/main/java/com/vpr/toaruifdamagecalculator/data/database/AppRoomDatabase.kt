package com.vpr.toaruifdamagecalculator.data.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vpr.toaruifdamagecalculator.data.model.BattleUnit
import com.vpr.toaruifdamagecalculator.data.model.DateBackup

@Database(entities = [BattleUnit::class, DateBackup::class], version = 1)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun dao() : Dao

    companion object {
        const val DB_NAME = "room_database"
    }
}