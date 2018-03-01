package me.leoyuu.mysqlclient.widget.row

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.sql.SqlCol

/**
 * date 2018/2/27
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class RowHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.table_row_detail_list_item, parent, false)) {
    private val colNameTv: TextView = itemView.findViewById(R.id.row_detail_col_name_tv)
    private val colInfoTv: TextView = itemView.findViewById(R.id.row_detail_col_info_tv)
    private val colValueTv: TextView = itemView.findViewById(R.id.row_detail_col_value_et)

    internal fun onBind(sqlCol: SqlCol, value: String?) {
        colNameTv.text = sqlCol.name
        colInfoTv.text = "${sqlCol.label}  ${sqlCol.size}  ${sqlCol.type}"
        colValueTv.text = value
    }

}
