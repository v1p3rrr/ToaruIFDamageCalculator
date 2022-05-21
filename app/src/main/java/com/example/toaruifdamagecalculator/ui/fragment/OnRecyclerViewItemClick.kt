package com.example.toaruifdamagecalculator.ui.fragment

import android.view.View

interface OnRecyclerViewItemClick<T> {
    fun onItemClick(view: View?, arg: T)
}