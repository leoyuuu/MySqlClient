package me.leoyuu.mysqlclient

import android.app.Application

/**
 * date 2018/2/25
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class App:Application() {

    override fun onCreate() {
        super.onCreate()
        app = this
    }

    companion object {
        lateinit var app:App
            private set
    }
}