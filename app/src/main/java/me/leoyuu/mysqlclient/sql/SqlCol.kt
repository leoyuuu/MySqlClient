package me.leoyuu.mysqlclient.sql

/**
 * date 2018/2/25
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
data class SqlCol(var name:String = "",
                  var type:String = TYPE_INT,
                  var size:String = "",
                  var scale:String = "",
                  var label:String = "",
                  var default:String = "",
                  var comment:String = "",
                  var nonnull:Boolean = false,
                  var primaryKey:Boolean = false){


    companion object {

        const val TYPE_TINYINT   = "TINYINT"
        const val TYPE_SMALLINT  = "SMALLINT"
        const val TYPE_MEDIUMINT = "MEDIUMINT"
        const val TYPE_INT       = "INT"
        const val TYPE_BIGINT    = "BIGINT"
        const val TYPE_FLOAT     = "FLOAT"
        const val TYPE_DOUBLE    = "DOUBLE"
        const val TYPE_DECIMAL   = "DECIMAL"

        const val TYPE_DATE      = "DATE"
        const val TYPE_TIME      = "TIME"
        const val TYPE_YEAR      = "YEAR"
        const val TYPE_DATETIME  = "DATETIME"
        const val TYPE_TIMESTAMP = "TIMESTAMP"

        const val TYPE_CHAR      = "CHAR"
        const val TYPE_VARCHAR   = "VARCHAR"
        const val TYPE_TINYBLOB  = "TINYBLOB"
        const val TYPE_BLOB      = "BLOB"
        const val TYPE_TEXT      = "TEXT"
        const val TYPE_MEDIUMBLOB= "MEDIUMBLOB"
        const val TYPE_MEDIUMTEXT= "MEDIUMTEXT"
        const val TYPE_LONGBLOB  = "LONGBLOB"
        const val TYPE_LONGTEXT  = "LONGTEXT"

        val TYPE_LIST = listOf(
                TYPE_INT, TYPE_CHAR, TYPE_VARCHAR, TYPE_TINYINT,
                TYPE_BIGINT, TYPE_DATE, TYPE_TEXT, TYPE_BLOB,
                TYPE_FLOAT, TYPE_TIME, TYPE_TIMESTAMP, TYPE_DATETIME,
                TYPE_MEDIUMINT, TYPE_MEDIUMBLOB, TYPE_MEDIUMTEXT, TYPE_DOUBLE,
                TYPE_SMALLINT, TYPE_DECIMAL, TYPE_YEAR, TYPE_TINYBLOB,
                TYPE_LONGBLOB, TYPE_LONGTEXT)

        val DEFAULT = SqlCol()
    }
}