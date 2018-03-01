package me.leoyuu.mysqlclient.module.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.sql.MySql
import me.leoyuu.mysqlclient.sql.ResultCallback
import me.leoyuu.mysqlclient.sql.SqlResult
import me.leoyuu.mysqlclient.util.JumpUtil
import me.leoyuu.mysqlclient.util.Util
import me.leoyuu.mysqlclient.widget.dialog.DialogProvideString


class MainActivity : AppCompatActivity() {
    private val adapter = DbAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()

        db_list.layoutManager = LinearLayoutManager(this)
        db_list.adapter = adapter
        db_list.addItemDecoration(DbDecorator())

        adapter.refresh()
    }

    private fun initToolbar(){
        setSupportActionBar(main_tool_bar)
        main_tool_bar.title = getString(R.string.main_title)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.action_cmd -> JumpUtil.gotoCmdActivity(this)
            R.id.action_db_new -> showCreateDbDialog()
            R.id.action_refresh -> adapter.refresh()
            R.id.action_view_record ->JumpUtil.gotoLogActivity(this)
        }
        return true
    }

    private fun showCreateDbDialog(){
        DialogProvideString.showDialog(this, "数据库名", "创建数据库", object :DialogProvideString.ConfirmCallback{
            override fun onCancel() {}

            override fun onSure(content: String) {
                MySql.getSql()?.createDb(content, object : ResultCallback{
                    override fun onResult(result: SqlResult) {
                        if (result.sqlOK){
                            Util.showToast("创建数据库成功")
                            adapter.refresh()
                        } else {
                            Util.showToast(result.errMsg)
                        }
                    }
                })
            }
        })
    }
}
