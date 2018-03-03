package me.leoyuu.mysqlclient.module.sqlcmd

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sql_cmd.*
import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.sql.MySql
import me.leoyuu.mysqlclient.sql.ResultCallback
import me.leoyuu.mysqlclient.sql.SqlResult
import me.leoyuu.mysqlclient.widget.dialog.DialogLoading

class SqlCmdActivity : AppCompatActivity() {

    private val adapter = SqlAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sql_cmd)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        sql_cmd_res_list.adapter = adapter
        sql_cmd_res_list.layoutManager = LinearLayoutManager(this)

        sql_cmd_btn.setOnClickListener {
            doSql()
        }

        clear_res_btn.setOnClickListener {
            adapter.clear()
        }
    }

    private fun doSql(){
        if (TextUtils.isEmpty(sql_cmd.text)){
            Toast.makeText(this, "Sql 命令为空", Toast.LENGTH_SHORT).show()
            return
        }
        val loading = DialogLoading()
        loading.title = "执行中"
        loading.show(fragmentManager, "执行sql")
        MySql.getSql().doSqlCmd(sql_cmd.text.toString(), object : ResultCallback {
            override fun onResult(result: SqlResult) {
                adapter.add(result)
                sql_cmd_res_list.scrollToPosition(adapter.itemCount - 1)
                loading.show(fragmentManager, "执行sql")
            }
        })
    }
}
