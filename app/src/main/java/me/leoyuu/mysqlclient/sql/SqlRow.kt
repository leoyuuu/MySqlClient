package me.leoyuu.mysqlclient.sql

import me.leoyuu.mysqlclient.model.BaseModel

/**
 * date 2018/2/25
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
data class SqlRow(val items:List<String?>){
    override fun toString() = items.toString()
}