package me.leoyuu.mysqlclient.module.sqlcmd

import android.view.View
import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.sql.SqlResult
import me.leoyuu.mysqlclient.widget.table.SqlTableView

/**
 * date 2018/2/26
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class QueryHolder(v:View) : BaseSqlHolder(v){
    private val res = v.findViewById<SqlTableView>(R.id.sql_cmd_query_res_table)

    override fun onBind(result: SqlResult) {
        super.onBind(result)
        if (result.queryTitle == null || result.queryContent == null){
            res.clear()
            return
        }
        res.bindData(result.queryTitle, result.queryContent)
    }
}