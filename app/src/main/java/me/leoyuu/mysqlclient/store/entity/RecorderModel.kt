package me.leoyuu.mysqlclient.store.entity

/**
 * date 2018/2/27
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

data class RecorderModel(val id: Int = 0, val time: Long = System.currentTimeMillis(), val cmd: String, val result: String) {

    override fun hashCode() = id

    override fun equals(other: Any?): Boolean {
        return other is RecorderModel && other.id == id
    }
}