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

        MySql.getSql()?.showCreateTableSql(dbName, tableName, object : ResultCallback{
            override fun onResult(result: SqlResult) {
                if (result.sqlOK){
                    table_detail_info_tv.text = result.queryContent!![0].items[1]
                } else {
                    table_detail_info_tv.error = result.errMsg
                }
            }
        })
        MySql.getSql()?.showTable(dbName, tableName, 0, 50, object : ResultCallback {
            override fun onResult(result: SqlResult) {
                if (result.sqlOK){
                    table_detail_table_view.bindData(result.queryTitle!!, result.queryContent!!)
                } else {
                    table_detail_info_tv.error = result.errMsg
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
                DialogConfirm.showDialog(this, "如果后面没有弹出文件权限申请，请在设置中同意使用该权限，否则本功能无法实现", object : DialogConfirm.ConfirmCallback {
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
        MySql.getSql()?.showTable(dbName, tableName, 0, Int.MAX_VALUE, object : ResultCallback {
            override fun onResult(result: SqlResult) {
                if (result.sqlOK) {
                    if (!fileThread.isAlive) {
                        fileThread.start()
                    }
                    exporting = true
                    Handler(fileThread.looper).post {
                        try {
                            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
                            val outFile = File(dir, "mysql_client_table_${tableName}_${System.currentTimeMillis()}.csv")
                            val os = FileOutputStream(outFile)
                            saveFile(result.queryTitle!!, result.queryContent!!, os)
                            os.close()
                            uiHandler.post { exportFinished(outFile) }
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

    private fun saveFile(title: QueryTitle, content: List<SqlRow>, os: OutputStream) {
        val sb = StringBuilder()
        title.titles.forEach {
            sb.append("\"${it.name}\",")
        }
        sb.append('\n')
        content.forEach {
            it.items.forEach { item ->
                sb.append("\"$item\",")
            }
            sb.append('\n')
            if (sb.length > 1024 * 1024) {
                os.write(sb.toString().toByteArray())
                sb.delete(0, sb.length)
            }
        }
        os.write(sb.toString().toByteArray())
    }
}
