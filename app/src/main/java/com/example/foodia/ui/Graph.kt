package com.example.foodia.ui

import android.content.Context
import com.example.foodia.model.DataStoreManager

object Graph {
    lateinit var dataStoreManager: DataStoreManager

    fun provide(context: Context) {
        dataStoreManager = DataStoreManager(context)
    }
}