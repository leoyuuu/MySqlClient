package me.leoyuu.mysqlclient.util

import android.support.annotation.StringRes
import android.widget.Toast
import me.leoyuu.mysqlclient.App
import java.text.SimpleDateFormat
import java.util.*

/**
 * date 2018/2/25
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
object Util {
    private val dateFormat = SimpleDateFormat("YYYY.MM.dd HH:mm:ss", Locale.CHINA)

    fun showToast(@StringRes msg:Int) = Toast.makeText(App.app, msg, Toast.LENGTH_SHORT).show()
    fun showToast(msg:String) = Toast.makeText(App.app, msg, Toast.LENGTH_SHORT).show()
    fun dp2pix(dp:Int) = (App.app.resources.displayMetrics.density * dp).toInt()

    fun formatDate(time: Long) = dateFormat.format(Date(time))
}