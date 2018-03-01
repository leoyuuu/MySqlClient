package me.leoyuu.mysqlclient.util

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.TestActivity
import me.leoyuu.mysqlclient.module.design.TableDesignActivity
import me.leoyuu.mysqlclient.module.main.MainActivity
import me.leoyuu.mysqlclient.module.log.LogActivity
import me.leoyuu.mysqlclient.module.sqlcmd.SqlCmdActivity
import me.leoyuu.mysqlclient.module.table.TableDetailActivity

/**
 * date 2018/2/26
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
object JumpUtil {

    fun gotoCmdActivity(context: Context) = context.startActivity(Intent(context, SqlCmdActivity::class.java))
    fun gotoMainActivity(context: Context) = context.startActivity(Intent(context, MainActivity::class.java))
    fun gotoLogActivity(context: Context) = context.startActivity(Intent(context, LogActivity::class.java))
    fun gotoTestActivity(context: Context) = context.startActivity(Intent(context, TestActivity::class.java))

    fun gotoTableDesignActivity(context: Context, dbName: String, tableName: String? = null){
        if (TextUtils.isEmpty(dbName)){
            Util.showToast(R.string.db_or_table_name_empty)
            return
        }

        val i = Intent(context, TableDesignActivity::class.java)
        i.putExtra(IntentConfig.DB_NAME, dbName)
        if (tableName != null){
            i.putExtra(IntentConfig.TABLE_NAME, tableName)
        }
        context.startActivity(i)
    }

    fun gotoTableDetailActivity(context: Context, dbName:String, tableName:String){
        if (TextUtils.isEmpty(dbName) || TextUtils.isEmpty(tableName)){
            Util.showToast(R.string.db_or_table_name_empty)
            return
        }

        val i = Intent(context, TableDetailActivity::class.java)
        i.putExtra(IntentConfig.DB_NAME, dbName)
        i.putExtra(IntentConfig.TABLE_NAME, tableName)
        context.startActivity(i)
    }
}