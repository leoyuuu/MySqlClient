package me.leoyuu.mysqlclient.module.design

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_table_design.*

import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.sql.MySql
import me.leoyuu.mysqlclient.sql.ResultCallback
import me.leoyuu.mysqlclient.sql.SqlResult
import me.leoyuu.mysqlclient.util.IntentConfig
import me.leoyuu.mysqlclient.util.Util
import me.leoyuu.mysqlclient.widget.dialog.DialogProvideString

/**
 * date 2018/2/28
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class TableDesignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_design)

        val dbName = intent.getStringExtra(IntentConfig.DB_NAME)
        val tableName = intent.getStringExtra(IntentConfig.TABLE_NAME)

        if (dbName.isNullOrEmpty()){
            Util.showToast("未指定数据库名时，可以在表名前增加其名字 如 dbSchool.tableStudent")
        } else {
            table_design_view.bindDbName(dbName)
        }

        if (!tableName.isNullOrEmpty()) {
            table_design_view.bindTable(tableName)
            table_design_view.visibility = View.GONE
        }


        table_design_create_btn.setOnClickListener {
            DialogProvideString.showDialog(this, "sql 命令", "执行 SQL？", contentValue = table_design_view.getSql(), callback = object :DialogProvideString.ConfirmCallback{
                override fun onCancel() {}

                override fun onSure(content: String) {
                    MySql.getSql().doSqlCmd(content, object : ResultCallback {
                        override fun onResult(result: SqlResult) {
                            if (result.sqlOK){
                                Util.showToast("执行成功，请刷新查看")
                                finish()
                            } else {
                                Util.showToast(result.errMsg)
                            }
                        }
                    })
                }
            })

        }
    }
}
