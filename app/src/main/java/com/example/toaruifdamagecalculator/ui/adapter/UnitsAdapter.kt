package com.example.toaruifdamagecalculator.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.toaruifdamagecalculator.R
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.ui.fragment.OnRecyclerViewItemClick

class UnitsAdapter(val clickListener: OnRecyclerViewItemClick<BattleUnit>) :
    ListAdapter<BattleUnit, UnitsAdapter.MyViewHolder>(DiffCallback()) {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val unitNameTv: TextView = itemView.findViewById(R.id.unitName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.characters_selection_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.unitNameTv.text = "${getItem(position).charName} | ${getItem(position).cardName}"
        holder.unitNameTv.setOnClickListener {
            clickListener.onItemClick(it, getItem(position))
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<BattleUnit>() {
        override fun areItemsTheSame(oldItem: BattleUnit, newItem: BattleUnit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BattleUnit, newItem: BattleUnit): Boolean {
            return oldItem == newItem
        }

    }
}