package me.leoyuu.mysqlclient.module.sqlcmd

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.sql.SqlResult

/**
 * date 2018/2/26
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class SqlAdapter : RecyclerView.Adapter<BaseSqlHolder>(){
    private val results = mutableListOf<SqlResult>()

    fun clear(){
        val size = results.size
        results.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun add(result: SqlResult){
        results.add(result)
        notifyItemInserted(results.size-1)
    }

    override fun getItemCount() = results.size

    override fun onBindViewHolder(holder: BaseSqlHolder?, position: Int) {
        holder?.onBind(results[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int):BaseSqlHolder{
        val v = LayoutInflater.from(parent!!.context).inflate(viewType, parent, false)
        return when (viewType){
            VIEW_TYPE_ERROR -> ErrorHolder(v)
            VIEW_TYPE_QUERY -> QueryHolder(v)
            else -> UpdateHolder(v)
        }
    }

    override fun getItemViewType(position: Int):Int {
        return if (!results[position].sqlOK){
            VIEW_TYPE_ERROR
        } else if (results[position].cmdType == SqlResult.CMD_TYPE_QUERY){
            VIEW_TYPE_QUERY
        } else {
            VIEW_TYPE_UPDATE
        }
    }

    companion object {
        val VIEW_TYPE_QUERY = R.layout.sql_result_query
        val VIEW_TYPE_UPDATE = R.layout.sql_result_update
        val VIEW_TYPE_ERROR = R.layout.sql_result_error
    }
}
