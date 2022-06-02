package com.vpr.toaruifdamagecalculator.data.model

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
    var imageUrl: String?,

    @ColumnInfo
    @SerializedName("imageSecondUrl")
    var imageSecondUrl: String? = null,

    @ColumnInfo
    @SerializedName("imageBias")
    var imageBias: String? = null,

    @ColumnInfo
    @SerializedName("color")
    var color: String? = null,

    @ColumnInfo
    @SerializedName("primaryAttack")
    var primaryAttack: String? = null,


    @ColumnInfo
    @SerializedName("skillAtkMultiplier")
    var skillAtkMultiplier: Int? = 1,

    @ColumnInfo
    @SerializedName("skillBonusMagic")
    var skillBonusMagic: Float? = 1F,

    @ColumnInfo
    @SerializedName("skillBonusScience")
    var skillBonusScience: Float? = 1F,

    @ColumnInfo
    @SerializedName("spAtkMultiplier")
    var spAtkMultiplier: Int? = 1,

    @ColumnInfo
    @SerializedName("spBonusType")
    var spBonusType: String? = null,

    @ColumnInfo
    @SerializedName("spBonusMultiplier")
    var spBonusMultiplier: Float? = 1F,


    @ColumnInfo
    @SerializedName("passive1Type")
    var passive1Type: String? = null,

    @ColumnInfo
    @SerializedName("passive1Activation")
    var passive1Activation: String? = null,

    @ColumnInfo
    @SerializedName("passive1Flat")
    var passive1Flat: String? = null,

    @ColumnInfo
    @SerializedName("passive2Type")
    var passive2Type: String? = null,

    @ColumnInfo
    @SerializedName("passive2Activation")
    var passive2Activation: String? = null,

    @ColumnInfo
    @SerializedName("passive2Flat")
    var passive2Flat: String? = null,

//    val attackTypeIds: List<Long>
) : Parcelable