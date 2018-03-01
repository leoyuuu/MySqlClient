package me.leoyuu.mysqlclient.sql

/**
 * date 2018/2/25
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
data class SqlResult(val code:Int = ERR_OK,
                     val errMsg:String="",
                     val cmdType:Int = CMD_TYPE_UPDATE,
                     val affectingRow:Int = 0,
                     val queryTitle:QueryTitle? = null,
                     val queryContent:List<SqlRow>? = null,
                     val sql:String = "") {

    val sqlOK:Boolean
    get() = code == ERR_OK

    override fun toString(): String {
        return if (sqlOK){
            if (cmdType == CMD_TYPE_UPDATE){
                "影响到$affectingRow"
            } else {
                "title:\n$queryTitle\ncontent:\n" + getContentString()
            }
        } else {
            errMsg
        }
    }

    private fun getContentString():String{
        if (queryContent == null || queryContent.isEmpty()){
            return "无数据"
        } else {
            var content = ""
            queryContent.forEach {
                content += it
                content += '\n'
            }
            return content
        }
    }

    companion object {
        val ERR_OK = 0
        val ERR_IO = -1
        val ERR_SQL = -2

        val CMD_TYPE_QUERY = 1
        val CMD_TYPE_UPDATE = 2
    }
}