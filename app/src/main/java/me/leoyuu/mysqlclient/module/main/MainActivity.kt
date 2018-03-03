package me.leoyuu.mysqlclient.module.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.model.DbModel
import me.leoyuu.mysqlclient.model.TableModel
import me.leoyuu.mysqlclient.sql.MySql
import me.leoyuu.mysqlclient.sql.ResultCallback
import me.leoyuu.mysqlclient.sql.SqlResult
import me.leoyuu.mysqlclient.util.JumpUtil
import me.leoyuu.mysqlclient.util.Util
import me.leoyuu.mysqlclient.widget.dialog.DialogLoading
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

        refresh()
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
            R.id.action_refresh -> refresh()
            R.id.action_view_record ->JumpUtil.gotoLogActivity(this)
            R.id.action_view_about -> JumpUtil.gotoAboutActivity(this)
        }
        return true
    }

    private fun refresh() {
        val loading = DialogLoading()
        loading.title = "加载中"
        loading.show(fragmentManager, "加载数据库信息")
        adapter.clear()
        MySql.getSql().showDataBases(object : ResultCallback {
            override fun onResult(result: SqlResult) {
                loading.dismiss()
                if (result.sqlOK) {
                    val dbs = result.queryContent?.filter { it.items[0] != null }?.map { it.items[0] as String }

                    dbs?.forEach {
                        MySql.getSql().showTables(it, object : ResultCallback {
                            override fun onResult(result: SqlResult) {
                                val tables = result.queryContent
                                if (result.sqlOK && tables != null) {
                                    adapter.addDb(DbModel(it, tables.map { table -> TableModel(table.items[0]!!) }.toMutableList()))
                                }
                            }
                        })
                    }
                } else {
                    Util.showToast("获取数据库信息失败，请退出app后打开重试")
                }
            }
        })
    }

    private fun showCreateDbDialog(){
        DialogProvideString.showDialog(this, "数据库名", "创建数据库", object :DialogProvideString.ConfirmCallback{
            override fun onCancel() {}

            override fun onSure(content: String) {
                val loading = DialogLoading()
                loading.title = "创建数据库..."
                loading.show(fragmentManager, "创建数据库...")
                MySql.getSql().createDb(content, object : ResultCallback {
                    override fun onResult(result: SqlResult) {
                        if (result.sqlOK){
                            Util.showToast("创建数据库成功")
                            refresh()
                        } else {
                            Util.showToast(result.errMsg)
                        }
                        loading.dismiss()
                    }
                })
            }
        })
    }
}
