package com.example.toaruifdamagecalculator.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BattleUnit (
    @SerializedName("id")
    var id : Long,

    @SerializedName("charName")
    var charName: String,

    @SerializedName("cardName")
    var cardName: String?,

    @SerializedName("imageUrl")
    var imageUrl: String?

) : Parcelable