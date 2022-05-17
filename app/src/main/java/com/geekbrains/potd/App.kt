package com.geekbrains.potd

import android.app.Application

class App : Application() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    var navigator: Navigator? = null
}