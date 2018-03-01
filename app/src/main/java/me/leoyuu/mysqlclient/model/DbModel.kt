package me.leoyuu.mysqlclient.model

/**
 * date 2018/2/26
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

data class DbModel(var name:String, val tableList: MutableList<TableModel> = mutableListOf(), var tableStatus:Int = TABLE_STATUS_HIDE) : BaseModel(){

    init {
        tableList.forEach { it.dbModel = this }
    }

    override fun toString() = name

    companion object {
        const val TABLE_STATUS_SHOW = 0
        const val TABLE_STATUS_HIDE = 1

        val DEFAULT_MODEL = DbModel("default")
    }
}
