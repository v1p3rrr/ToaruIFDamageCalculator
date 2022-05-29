package com.example.toaruifdamagecalculator.ui.search

import android.view.View

interface OnRecyclerViewItemClick<T> {
    fun onItemClick(view: View?, arg: T)
}