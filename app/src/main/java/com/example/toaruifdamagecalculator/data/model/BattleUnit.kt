package com.example.toaruifdamagecalculator.data.model

import com.google.gson.annotations.SerializedName

data class BattleUnit (
    @SerializedName("id")
    var id : Long,

    @SerializedName("charName")
    var charName: String,

    @SerializedName("cardName")
    var cardName: String,

    @SerializedName("imageUrl")
    var imageUrl: String

)