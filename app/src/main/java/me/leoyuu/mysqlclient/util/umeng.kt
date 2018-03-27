package me.leoyuu.mysqlclient.util

import android.content.Context
import com.umeng.analytics.MobclickAgent

/**
 * date 2018/3/3
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
const val UMENG_EVENT_CONNECT = "connect_sql"

fun reportUmeng(context: Context, eventId: String) {
    MobclickAgent.onEvent(context, eventId)
}