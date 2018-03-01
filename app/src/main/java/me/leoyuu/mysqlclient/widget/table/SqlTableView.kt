package me.leoyuu.mysqlclient.widget.table

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.GridLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.sql_table_view.view.*

import java.util.ArrayList

import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.sql.QueryTitle
import me.leoyuu.mysqlclient.sql.SqlCol
import me.leoyuu.mysqlclient.sql.SqlRow

/**
 * date 2018/2/26
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class SqlTableView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {
    private var title = QueryTitle()
    private var rows: List<SqlRow> = listOf()

    private val titleAdapter = TitleAdapter()
    private val contentAdapter = ContentAdapter()

    init {
        LayoutInflater.from(context).inflate(R.layout.sql_table_view, this)
        sql_table_title_list.adapter = titleAdapter
        sql_table_body_list.adapter = contentAdapter

    }

    fun bindData(title: QueryTitle, rows: List<SqlRow>) {
        this.title = title
        this.rows = rows

        val col = if (title.size > 4) 4 else title.size

        sql_table_title_list.layoutManager = GridLayoutManager(context, col)
        sql_table_body_list.layoutManager = GridLayoutManager(context, col)

        titleAdapter.updateTitles(title)
        contentAdapter.updateContent(rows, title)
    }

    fun clear(){
        title = QueryTitle()
        rows = listOf()
        titleAdapter.updateTitles(title)
        contentAdapter.updateContent(rows, title)
    }
}
