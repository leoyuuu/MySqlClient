package me.leoyuu.mysqlclient.widget.design

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.sql_table_design_view.view.*
import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.sql.MySql
import me.leoyuu.mysqlclient.sql.ResultCallback
import me.leoyuu.mysqlclient.sql.SqlCol
import me.leoyuu.mysqlclient.sql.SqlResult
import me.leoyuu.mysqlclient.util.Util

/**
 * date 2018/2/28
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class TableDesignView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val sqlCols = mutableListOf<SqlCol>()
    private val adapter = TableColAdapter()
    private var dbName:String = ""
    private var tableName = ""

    init {
        LayoutInflater.from(context).inflate(R.layout.sql_table_design_view, this)
        table_design_col_list.layoutManager = LinearLayoutManager(context)
        table_design_col_list.adapter = adapter
        table_design_col_add.setOnClickListener { adapter.addCol() }
    }

    fun bindDbName(dbName:String){
        this.dbName = dbName
    }

    fun bindTable(tableName:String){
        this.tableName = tableName
        table_design_table_name_et.setText("$dbName.$tableName")
        MySql.getSql()?.showTableColumns(dbName, tableName, object : ResultCallback{
            override fun onResult(result: SqlResult) {
                if (result.sqlOK) {
                    visibility = View.VISIBLE
                    result.queryContent!!.forEach {
                        val col = it.items
                        val name = col[0]
                        val nullable = col[1] == "YES"
                        val dataType = col[2]
                        val size = if (col[3] == null) "" else col[3]
                        val scale = if (col[4] == null) "" else col[4]
                        val comment = if (col[5] == null) "" else col[5]
                        val default = if (col[6] == null) "" else col[6]
                        val key = col[7] == "PRI"
                        sqlCols.add(SqlCol(name!!, dataType!!, size!!, scale!!, "", default!!, comment!!, !nullable, key))
                    }
                    sqlCols.forEach { adapter.addCol(it.copy()) }
                } else {
                    Util.showToast(result.errMsg)
                }
            }
        })
    }

    fun getSql():String {
        val tableName = table_design_table_name_et.text!!.toString()
        if (tableName.isEmpty()){
            table_design_table_name_et.error = "这里需要填入表名"
            Util.showToast("表名不能为空")
            return ""
        }
        var fullName = ""
        if (tableName.contains('.')){
            fullName = tableName
        } else {
            fullName = dbName + "." + tableName
        }

        return "CREATE TABLE $fullName (\n${adapter.getColsSql().replace("{table_name}", fullName)})DEFAULT CHARSET=utf8"
    }
}
