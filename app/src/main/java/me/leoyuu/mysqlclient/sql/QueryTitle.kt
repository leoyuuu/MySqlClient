package me.leoyuu.mysqlclient.sql

/**
 * date 2018/2/25
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
data class QueryTitle(val titles:List<SqlCol> = listOf()){
    val size
    get() = titles.size
}