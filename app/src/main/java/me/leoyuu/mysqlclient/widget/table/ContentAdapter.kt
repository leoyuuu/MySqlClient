package me.leoyuu.mysqlclient.widget.table

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.sql.QueryTitle
import me.leoyuu.mysqlclient.sql.SqlRow

/**
 * date 2018/2/26
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class ContentAdapter : RecyclerView.Adapter<ContentItemHolder>() {
    private var title = QueryTitle()
    private var col = 0
    private var rows = listOf<SqlRow>()

    fun updateContent(rows:List<SqlRow>, title:QueryTitle){
        val size = itemCount
        if (size > 0) {
            notifyItemRangeRemoved(0, size)
        }
        if (rows.isNotEmpty()){
            this.rows = rows
            this.col = rows[0].items.size
            this.title = title;
            notifyItemRangeInserted(0, itemCount)
        }
    }

    override fun onBindViewHolder(holder: ContentItemHolder, position: Int) {
        val col = if (this.col > MAX_COL){
            MAX_COL
        } else {
            this.col
        }
        holder.onBind(rows[position / col].items[position % col], title, rows[position/col])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ContentItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.sql_table_content_item, parent, false))


    override fun getItemCount(): Int {
        return if (col > MAX_COL) {
            MAX_COL * rows.size
        } else {
            col * rows.size
        }
    }

    companion object {
        const val MAX_COL = 4
    }
}
