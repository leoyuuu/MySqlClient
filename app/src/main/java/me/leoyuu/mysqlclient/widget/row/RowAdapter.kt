package me.leoyuu.mysqlclient.widget.row

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import me.leoyuu.mysqlclient.sql.SqlCol

/**
 * date 2018/2/27
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class RowAdapter(private val sqlCols:List<SqlCol>, private val rowValue:List<String?>) : RecyclerView.Adapter<RowHolder>() {


    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        holder.onBind(sqlCols[position], rowValue[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        return RowHolder(parent)
    }

    override fun getItemCount(): Int {
        return sqlCols.size
    }

}
