package com.vpr.toaruifdamagecalculator.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "date")
data class DateBackup(

    @ColumnInfo
    @SerializedName("timeInMillis")
    var timeInMillis: Long

) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
