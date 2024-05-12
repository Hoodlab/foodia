package com.example.foodia

import android.app.Application
import com.example.foodia.ui.Graph

class FoodiaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(context = this)
    }
}