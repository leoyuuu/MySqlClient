package me.leoyuu.mysqlclient.widget.row

import android.app.DialogFragment
import android.app.FragmentManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.table_row_detail_dialog.*

import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.sql.QueryTitle
import me.leoyuu.mysqlclient.sql.SqlRow
import me.leoyuu.mysqlclient.util.Util
import me.leoyuu.mysqlclient.widget.SpaceItemDecorator

/**
 * date 2018/2/27
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class RowDetailDialog : DialogFragment() {
    private lateinit var title: QueryTitle
    private lateinit var row: SqlRow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.table_row_detail_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        row_detail_col_info_list.adapter = RowAdapter(title.titles, row.items)
        row_detail_col_info_list.layoutManager = LinearLayoutManager(activity)

        val decoration = SpaceItemDecorator()
        decoration.setOffset(bottom = Util.dp2pix(8))
        row_detail_col_info_list.addItemDecoration(decoration)
    }

    fun bindRow(title: QueryTitle, row:SqlRow){
        this.title = title
        this.row = row
    }

    companion object {
        fun showDialog(context: Context, title: QueryTitle, row: SqlRow) {
            val dialog = RowDetailDialog()
            dialog.bindRow(title, row)
            dialog.show((context as AppCompatActivity).fragmentManager, title.toString())
        }
    }
}