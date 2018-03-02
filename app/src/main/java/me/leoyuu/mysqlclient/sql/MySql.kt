package me.leoyuu.mysqlclient.sql

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import me.leoyuu.mysqlclient.store.DbHelper
import me.leoyuu.mysqlclient.store.entity.RecorderModel
import java.io.IOException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/**
 * date 2018/2/23
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class MySql(private val conn: Connection) {

    fun showTableColumns(dbName:String, tableName:String, callback: ResultCallback) = postSqlCmd(getTableColumnsInfo(dbName, tableName), callback)
    fun alterTableName(dbName:String, tableName:String, newTableName:String, callback: ResultCallback) = postSqlCmd(getAlterTableNameSql(dbName, tableName, newTableName), callback)

    fun showTables(dbName:String, callback: ResultCallback) = postSqlCmd(getShowTablesSql(dbName), callback)
    fun showCreateTableSql(dbName:String, table:String, callback: ResultCallback) = postSqlCmd(getTableSql(dbName, table), callback)
    fun showTable(dbName:String, table:String, startIndex:Int = 0, num:Int = 10, callback: ResultCallback) = postSqlCmd(getShowTableSql(dbName, table, startIndex, num), callback)
    fun deleteTable(dbName: String, table: String, callback: ResultCallback) = postSqlCmd(getDelTableSql(dbName, table), callback)

    fun createDb(dbName: String, callback: ResultCallback) = postSqlCmd(getDbCreateSql(dbName), callback)
    fun delDb(dbName: String, callback: ResultCallback) = postSqlCmd(getDelDbSql(dbName), callback)
    fun showDataBases(callback: ResultCallback) = postSqlCmd(SHOW_DB, callback)

    fun doSqlCmd(cmd:String, callback: ResultCallback) = postSqlCmd(cmd, callback)

    private fun postSqlCmd(cmd:String, callback: ResultCallback) = sqlHandler.post { doSqlCmdInternal(cmd, callback)}

    private fun doSqlCmdInternal(cmd:String, callback: ResultCallback){
        try {
            val cmdCheck = cmd.toUpperCase()
            if (cmdCheck.startsWith("SELECT") || cmdCheck.startsWith("SHOW") || cmdCheck.startsWith("DESC")){
                val rs = conn.createStatement().executeQuery(cmd)
                val metaData = rs.metaData
                val count = metaData.columnCount
                val sqlTitle = mutableListOf<SqlCol>()
                (1 .. count).forEach {
                    val name = metaData.getColumnName(it)
                    val type = metaData.getColumnTypeName(it)
                    val size = metaData.getColumnDisplaySize(it)
                    val label = metaData.getColumnLabel(it)
                    sqlTitle.add(SqlCol(name, type, size.toString(), label))
                }
                val sqlRows = mutableListOf<SqlRow>()
                while (rs.next()){
                    sqlRows.add(SqlRow((1..count).map { return@map when(rs.metaData.getColumnTypeName(it)){
                        "BLOB" -> "(Blob)"
                        "CLOB" -> "(Clob)"
                        "NCLOB" -> "(NClob)"
                        else -> rs.getString(it)
                    } }))
                }
                val res = SqlResult(affectingRow = sqlRows.size, cmdType = SqlResult.CMD_TYPE_QUERY, queryTitle = QueryTitle(sqlTitle), queryContent = sqlRows.toList(), sql = cmd)
                DbHelper.saveRecorder(RecorderModel(cmd = cmd, result = res.toString()))
                doResult(callback, res)
            } else {
                val rs = conn.createStatement().executeUpdate(cmd)
                DbHelper.saveRecorder(RecorderModel(cmd = cmd, result = "affect $rs"))
                doResult(callback, SqlResult(affectingRow = rs, sql = cmd))
            }
        } catch (e:SQLException){
            DbHelper.saveRecorder(RecorderModel(cmd = cmd, result = e.localizedMessage))
            doResult(callback, SqlResult(SqlResult.ERR_SQL, e.localizedMessage, sql = cmd))
        } catch (e:Exception) {
            mySqlInternal = null
            doResult(callback, SqlResult(SqlResult.ERR_IO, e.localizedMessage, sql = cmd))
        }
    }

    companion object {
        private val handleThread = HandlerThread(MySql::class.java.simpleName)
        private val uiHandler = Handler(Looper.getMainLooper())

        private lateinit var sqlHandler:Handler

        fun getSql(): MySql? {
            return mySqlInternal
        }

        fun init(host:String, port:Int, name:String, password:String, callback: ResultCallback){
            if (!handleThread.isAlive){
                handleThread.start()
                sqlHandler = Handler(handleThread.looper)
            }

            sqlHandler.post {
                try {
                    Class.forName(DRIVER_NAME)
                    val url = "jdbc:mysql://$host:$port?characterEncoding=utf-8&amp;useSSL=false&amp;useUnicode=true"
                    val conn = DriverManager.getConnection(url, name, password)
                    if (conn != null){
                        mySqlInternal = MySql(conn)
                        doResult(callback, SqlResult())
                    }
                } catch (e:SQLException) {
                    doResult(callback, SqlResult(SqlResult.ERR_SQL, e.localizedMessage))
                } catch (e:IOException) {
                    doResult(callback, SqlResult(SqlResult.ERR_IO, e.localizedMessage))
                }

            }
        }


        private fun doResult(callback: ResultCallback, result: SqlResult) = uiHandler.post { callback.onResult(result) }

        private var mySqlInternal: MySql? = null

        private const val DRIVER_NAME = "com.mysql.jdbc.Driver"

        private const val SHOW_DB = "show databases"
        private fun getShowTablesSql(dbName:String) = "SELECT table_name FROM information_schema.tables WHERE table_schema = '$dbName'"
        private fun getShowTableSql(dbName:String, table:String, startIndex: Int, num: Int) = "SELECT * FROM $dbName.$table LIMIT $startIndex, $num"
        private fun getTableSql(dbName:String, tableName:String) = "SHOW CREATE TABLE $dbName.$tableName"
        private fun getDelTableSql(dbName:String, tableName:String) = "DROP TABLE $dbName.$tableName"
        private fun getDelDbSql(dbName:String) = "DROP DATABASE $dbName"
        private fun getDbCreateSql(dbName: String) = "CREATE DATABASE IF NOT EXISTS $dbName DEFAULT CHARSET utf8 COLLATE utf8_general_ci;"
        private fun getTableColumnsInfo(dbName: String, tableName: String) = "SELECT column_name, is_nullable, data_type, character_maximum_length, numeric_scale, column_comment, column_default, column_key FROM information_schema.columns WHERE table_name = '$tableName' and table_schema = '$dbName'"
        private fun getAlterTableNameSql(dbName: String, tableName: String, newTableName: String) = "ALTER TABLE $dbName.$tableName RENAME TO $dbName.$newTableName"

    }
}