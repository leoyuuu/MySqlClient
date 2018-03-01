package me.leoyuu.mysqlclient.widget.table

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.sql.QueryTitle

/**
 * date 2018/2/26
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class TitleAdapter : RecyclerView.Adapter<TitleItemHolder>() {
    private var title = QueryTitle()

    fun updateTitles(title:QueryTitle){
        val size = itemCount
        if (size > 0) {
            notifyItemRangeRemoved(0, size)
        }
        if (title.size > 0){
            this.title = title
            notifyItemRangeInserted(0, title.size)
        }
    }

    override fun onBindViewHolder(holder: TitleItemHolder, position: Int) {
        holder.onBind(title.titles[position].name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            TitleItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.sql_table_title_item, parent, false))


    override fun getItemCount(): Int {
        return if (title.size > MAX_COL) {
            MAX_COL
        } else {
            title.size
        }
    }

    companion object {
        const val MAX_COL = 4
    }
}
