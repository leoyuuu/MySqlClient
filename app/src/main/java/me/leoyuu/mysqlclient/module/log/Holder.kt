package me.leoyuu.mysqlclient.module.log

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.store.entity.RecorderModel
import me.leoyuu.mysqlclient.util.Util

/**
 * date 2018/3/2
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class Holder(v: View) : RecyclerView.ViewHolder(v) {
    private val resTv: TextView = v.findViewById(R.id.sql_log_item_res)
    private val cmdTv: TextView = v.findViewById(R.id.sql_log_item_sql)
    private val timeTv: TextView = v.findViewById(R.id.sql_log_item_time)

    fun onBind(recorder: RecorderModel) {
        resTv.text = recorder.result
        cmdTv.text = recorder.cmd
        timeTv.text = Util.formatDate(recorder.time)
    }
}