package me.leoyuu.mysqlclient.model

/**
 * date 2018/2/27
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

data class RecorderModel(val time:Long, val cmd:String, val result:String, val success:Boolean = true) : BaseModel()