package me.leoyuu.mysqlclient.util

import android.content.Context
import me.leoyuu.mysqlclient.App

/**
 * date 2018/3/2
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
object PrefUtil {
    const val KEY_HOST = "ip_host"
    const val KEY_PORT = "host_port"
    const val KEY_USER = "user"

    private val pref = App.app.getSharedPreferences("MySqlClient", Context.MODE_PRIVATE)

    fun putString(key: String, value: String) = pref.edit().putString(key, value).apply()
    fun getString(key: String) = pref.getString(key, "")


    fun putInt(key: String, value: Int) = pref.edit().putInt(key, value).apply()
    fun getInt(key: String) = pref.getInt(key, 0)
}