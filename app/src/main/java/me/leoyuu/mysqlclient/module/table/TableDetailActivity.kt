package me.leoyuu.mysqlclient.module.table

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_table_detail.*
import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.sql.MySql
import me.leoyuu.mysqlclient.sql.ResultCallback
import me.leoyuu.mysqlclient.sql.SqlResult
import me.leoyuu.mysqlclient.util.IntentConfig

class TableDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_detail)
        setSupportActionBar(table_detail_toolbar)

        val dbName = intent.getStringExtra(IntentConfig.DB_NAME)
        val tableName = intent.getStringExtra(IntentConfig.TABLE_NAME)

        table_detail_toolbar.title = "$dbName.$tableName"
        table_detail_toolbar.inflateMenu(R.menu.menu_table_view)

        MySql.getSql()?.showCreateTableSql(dbName, tableName, object : ResultCallback{
            override fun onResult(result: SqlResult) {
                if (result.sqlOK){
                    table_detail_info_tv.text = result.queryContent!![0].items[1]
                } else {
                    table_detail_info_tv.error = result.errMsg
                }
            }
        })
        MySql.getSql()?.showTable(dbName, tableName, 0, 1000, object :ResultCallback{
            override fun onResult(result: SqlResult) {
                if (result.sqlOK){
                    table_detail_table_view.bindData(result.queryTitle!!, result.queryContent!!)
                } else {
                    table_detail_info_tv.error = result.errMsg
                }
            }
        })
    }
}
