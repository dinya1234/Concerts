package com.example.kt_5

import android.app.Application
import com.example.kt_5.di.AppModule

class ConcertApp : Application() {
    lateinit var appModule: AppModule

    override fun onCreate() {
        super.onCreate()
        appModule = AppModule(this)
    }
}