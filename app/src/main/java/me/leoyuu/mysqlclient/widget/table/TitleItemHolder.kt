package me.leoyuu.mysqlclient.widget.table

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.sql.SqlCol

/**
 * date 2018/2/26
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class TitleItemHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val titleItemTv: TextView = view.findViewById(R.id.sql_table_title_tv)

    internal fun onBind(name: String) {
        titleItemTv.text = name
    }
}
