package com.example.toaruifdamagecalculator.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.toaruifdamagecalculator.R
import com.example.toaruifdamagecalculator.data.model.BattleUnit

class UnitsAdapter(private val unitItems: List<BattleUnit>) :
    RecyclerView.Adapter<UnitsAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val unitNameTv : TextView = itemView.findViewById(R.id.unitName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.characters_selection_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.unitNameTv.text = "${unitItems[position].charName} | ${unitItems[position].cardName}"
    }

    override fun getItemCount(): Int {
        return unitItems.size
    }
}