package me.leoyuu.mysqlclient.module.sqlcmd

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.model.BaseModel
import me.leoyuu.mysqlclient.sql.SqlResult

/**
 * date 2018/2/26
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

open class BaseSqlHolder(v: View) : RecyclerView.ViewHolder(v){
    private lateinit var result:SqlResult
    val sql:TextView = v.findViewById<TextView>(R.id.sql_cmd_tv)

    open fun onBind(result: SqlResult){
        this.result = result
        sql.text = result.sql
    }
}