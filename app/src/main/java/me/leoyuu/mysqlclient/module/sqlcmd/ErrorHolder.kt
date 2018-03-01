package me.leoyuu.mysqlclient.module.sqlcmd

import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.model.BaseModel
import me.leoyuu.mysqlclient.model.TableModel
import me.leoyuu.mysqlclient.sql.SqlResult
import me.leoyuu.mysqlclient.util.Util

/**
 * date 2018/2/26
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class ErrorHolder(v:View) : BaseSqlHolder(v){
    private val res = v.findViewById<TextView>(R.id.sql_cmd_error_tv)

    override fun onBind(result: SqlResult) {
        super.onBind(result)
        res.text = result.errMsg
    }
}