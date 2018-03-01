package me.leoyuu.mysqlclient.widget.design

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.sql.SqlCol

/**
 * date 2018/2/28
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class TableColAdapter : RecyclerView.Adapter<TableColItemHolder>() {
    private val cols = mutableListOf<SqlCol>()

    fun getColsSql():String {
        var res = ""
        val pks = mutableListOf<String>()
        cols.forEach {
            if (it.name.isNotEmpty()){
                res += it.name + " " + it.type
                if (it.size.isNotEmpty()){
                    if (it.type == SqlCol.TYPE_DECIMAL){
                        res += "(${it.size},${it.scale})"
                    } else {
                        res += "(" + it.size + ")"
                    }
                }
                if (it.nonnull){
                    res += " NOT NULL"
                }

                if (it.default.isNotEmpty()){
                    res += " default '${it.default}'"
                }

                if (it.comment.isNotEmpty()){
                    res += " comment '${it.comment}'"
                }
                res += ",\n"
                if (it.primaryKey) {
                    pks.add(it.name)
                }
            }
        }

        if (pks.size > 1){
            res += "CONSTRAINT pk_{table_name} PRIMARY KEY ${pks.toString().replace('[', '(').replace(']', ')')}\n"
        } else if (pks.size == 1){
            res += "PRIMARY KEY (${pks[0]})\n"
        } else if (res.isNotEmpty()){
            res = res.substring(0, res.length-1)
        }

        return res
    }

    fun addCol(col:SqlCol? = null){
        if (col != null){
            cols.add(col)
        } else {
            cols.add(SqlCol())
        }
        notifyItemInserted(cols.size-1)
    }

    fun onDel(position: Int){
        cols.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onBindViewHolder(holder: TableColItemHolder, position: Int) {
        holder.onBind(cols[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableColItemHolder {
        return TableColItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.sql_table_design_item_view, parent, false), this)
    }

    override fun getItemCount(): Int {
        return cols.size
    }
}
