package com.example.toaruifdamagecalculator.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class BattleUnit (
    @PrimaryKey
    @SerializedName("id")
    var id : Long,

    @ColumnInfo
    @SerializedName("charName")
    var charName: String,

    @ColumnInfo
    @SerializedName("cardName")
    var cardName: String,

    @ColumnInfo
    @SerializedName("imageUrl")
    var imageUrl: String?

) : Parcelable