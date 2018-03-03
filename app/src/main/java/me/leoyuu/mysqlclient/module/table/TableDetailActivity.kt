package me.leoyuu.mysqlclient.module.table

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_table_detail.*
import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.sql.*
import me.leoyuu.mysqlclient.util.IntentConfig
import me.leoyuu.mysqlclient.util.Util
import me.leoyuu.mysqlclient.widget.dialog.DialogConfirm
import me.leoyuu.mysqlclient.widget.dialog.DialogLoading
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class TableDetailActivity : AppCompatActivity() {
    private lateinit var dbName: String
    private lateinit var tableName: String

    private var exporting = false

    private val fileThread = HandlerThread(TableDetailActivity::class.java.simpleName)

    private val uiHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_detail)
        setSupportActionBar(table_detail_toolbar)

        dbName = intent.getStringExtra(IntentConfig.DB_NAME)
        tableName = intent.getStringExtra(IntentConfig.TABLE_NAME)

        table_detail_toolbar.title = "$dbName.$tableName"
        table_detail_toolbar.inflateMenu(R.menu.menu_table_view)

        val loading = DialogLoading()
        loading.title = "加载表结构信息"
        loading.show(fragmentManager, "加载中")
        MySql.getSql().showCreateTableSql(dbName, tableName, object : ResultCallback {
            override fun onResult(result: SqlResult) {
                if (result.sqlOK){
                    loading.title = "加载表数据"
                    table_detail_info_tv.text = result.queryContent!![0].items[1]
                    MySql.getSql().showTable(dbName, tableName, 0, 50, object : ResultCallback {
                        override fun onResult(result: SqlResult) {
                            if (result.sqlOK) {
                                table_detail_table_view.bindData(result.queryTitle!!, result.queryContent!!)
                            } else {
                                table_detail_info_tv.error = result.errMsg
                            }
                            loading.dismiss()
                        }
                    })
                } else {
                    table_detail_info_tv.error = result.errMsg
                    loading.dismiss()
                }
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_table_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_db_table_export -> export()
        }
        return true
    }

    private fun export() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                DialogConfirm.showDialog(this, "如果没有弹出文件权限申请，请在设置中同意使用该权限，否则本功能无法实现", object : DialogConfirm.ConfirmCallback {
                    override fun onCancel() {}

                    override fun onSure() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 123)
                        }
                    }
                })
            } else {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    doExport()
                } else {
                    requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 123)
                }
            }
        } else {
            doExport()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            doExport()
        } else {
            Util.showToast("取消使用")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fileThread.quit()
    }

    override fun onBackPressed() {
        if (exporting) {
            Util.showToast("正在导出文件，请稍后")
            return
        }

        super.onBackPressed()
    }

    private fun doExport() {
        val loading = DialogLoading()
        loading.title = "获取数据库内容"
        loading.show(fragmentManager, "导出")
        MySql.getSql().showTable(dbName, tableName, 0, Int.MAX_VALUE, object : ResultCallback {
            override fun onResult(result: SqlResult) {
                if (result.sqlOK) {
                    if (!fileThread.isAlive) {
                        fileThread.start()
                    }
                    exporting = true
                    loading.title = "导出中"
                    Handler(fileThread.looper).post {
                        try {
                            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
                            val outFile = File(dir, "mysql_client_table_${tableName}_${System.currentTimeMillis()}.csv")
                            val os = FileOutputStream(outFile)
                            saveFile(result.queryTitle!!, result.queryContent!!, os, loading)
                            os.close()
                            uiHandler.postDelayed({
                                loading.dismiss()
                                exportFinished(outFile)
                            }, 200)
                        } catch (e: IOException) {
                            Util.showToast("存储文件失败")
                        }
                    }
                } else {
                    Util.showToast(result.errMsg)
                }
            }
        })
    }

    private fun exportFinished(file: File) {
        exporting = false
        Snackbar.make(table_detail_table_view, "文件保存成功，建议使用表格工具打开，或纯文本方式查看", Snackbar.LENGTH_LONG).setAction("打开", {
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(Uri.fromFile(file), "text/plain")
            startActivity(intent)
        }).show()
    }

    private fun saveFile(title: QueryTitle, content: List<SqlRow>, os: OutputStream, loading: DialogLoading) {
        val sb = StringBuilder()
        title.titles.forEach {
            sb.append("\"${it.name}\",")
        }
        sb.deleteCharAt(sb.lastIndex)
        sb.append('\n')
        uiHandler.post {
            loading.max = content.size
        }
        var index = 0
        val invalidateInterval = if (content.size > 200) content.size / 99 else 1
        content.forEach {
            it.items.forEach { item ->
                sb.append("\"$item\",")
            }
            sb.deleteCharAt(sb.lastIndex)
            sb.append('\n')
            if (sb.length > 1024 * 1024) {
                os.write(sb.toString().toByteArray())
                sb.delete(0, sb.length)
            }
            index++
            if (index % invalidateInterval == 0) {
                uiHandler.post {
                    loading.current = index
                }
            }
        }
        os.write(sb.toString().toByteArray())
        uiHandler.post {
            loading.title = "完成"
            loading.current = loading.max
        }
    }
}
