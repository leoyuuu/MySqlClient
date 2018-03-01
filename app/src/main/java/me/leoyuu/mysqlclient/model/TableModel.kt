package me.leoyuu.mysqlclient.model

/**
 * date 2018/2/26
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class TableModel(var tableName:String, var dbModel:DbModel = DbModel.DEFAULT_MODEL) :BaseModel(){
    override fun toString(): String {
        return "$dbModel.$tableName"
    }
}
