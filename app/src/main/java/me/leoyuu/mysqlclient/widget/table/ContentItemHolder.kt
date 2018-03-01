package me.leoyuu.mysqlclient.widget.table

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast

import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.sql.QueryTitle
import me.leoyuu.mysqlclient.sql.SqlRow
import me.leoyuu.mysqlclient.util.Util
import me.leoyuu.mysqlclient.widget.row.RowDetailDialog

/**
 * date 2018/2/26
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class ContentItemHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val contentItemTv: TextView = view.findViewById(R.id.sql_table_content_tv)
    private lateinit var title:QueryTitle
    private lateinit var row: SqlRow

    init {
        view.setOnClickListener {
            RowDetailDialog.showDialog(itemView.context, title, row)
        }
    }

    internal fun onBind(name: String?, title: QueryTitle, row: SqlRow) {
        contentItemTv.text = name
        this.title = title
        this.row = row
    }
}
