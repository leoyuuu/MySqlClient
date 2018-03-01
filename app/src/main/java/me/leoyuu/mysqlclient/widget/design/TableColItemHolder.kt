package me.leoyuu.mysqlclient.widget.design

import android.support.v7.widget.RecyclerView
import android.view.View

import me.leoyuu.mysqlclient.sql.SqlCol

/**
 * date 2018/2/28
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class TableColItemHolder(view: View, val adapter: TableColAdapter) : RecyclerView.ViewHolder(view) {
    private val colView = itemView as TableColView

    fun onBind(col: SqlCol) = colView.onBind(col, this)
    fun onDel() = adapter.onDel(adapterPosition)
}
