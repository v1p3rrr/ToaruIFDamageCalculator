package com.example.toaruifdamagecalculator.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "date")
data class DateBackup(

    @ColumnInfo
    @SerializedName("timeInMillis")
    var timeInMillis: Long

) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
