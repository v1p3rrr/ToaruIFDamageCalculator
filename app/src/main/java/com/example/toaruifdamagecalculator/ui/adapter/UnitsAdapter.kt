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
import okhttp3.internal.filterList
import java.util.*

class UnitsAdapter(val clickListener: OnRecyclerViewItemClick<BattleUnit>) :
    ListAdapter<BattleUnit, UnitsAdapter.MyViewHolder>(DiffCallback()) {

    private var unfilteredList = listOf<BattleUnit>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

    //todo hide submitList(impossible) or how to make it better?
    // use this instead of submitList outside of the class
    fun modifyList(list: List<BattleUnit>) {
        unfilteredList = list
        submitList(list)
    }

    fun filter(query: CharSequence?) {
        val list = mutableListOf<BattleUnit>()
        val queryList = query?.split(Regex("\\W"))

        // perform the data filtering
        if (query.isNullOrEmpty()) {
            list.addAll(unfilteredList)
        } else {
            list.addAll(unfilteredList.filter {
                checkQueryEntry(it, queryList)
            })
        }
        submitList(list)
    }


    private fun checkQueryEntry(unit: BattleUnit, queryList: List<String>?) : Boolean{
        queryList?.let {
            for (queryWord in it) {
                queryWord.let {
                    if (!(unit.charName.contains(queryWord, ignoreCase = true) || unit.cardName.contains(queryWord, ignoreCase = true)))
                        return false
                }
            }
            return true
        }
        return false
    }

}