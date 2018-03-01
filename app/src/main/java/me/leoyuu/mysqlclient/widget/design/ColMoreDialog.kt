package me.leoyuu.mysqlclient.widget.design

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
import kotlinx.android.synthetic.main.sql_table_design_col_more_dialog_view.*
import kotlinx.android.synthetic.main.table_row_detail_dialog.*

import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.sql.QueryTitle
import me.leoyuu.mysqlclient.sql.SqlCol
import me.leoyuu.mysqlclient.sql.SqlRow
import me.leoyuu.mysqlclient.util.Util
import me.leoyuu.mysqlclient.widget.SpaceItemDecorator

/**
 * date 2018/2/27
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class ColMoreDialog : DialogFragment() {
    private var col = SqlCol.DEFAULT
    private lateinit var callback: ColumnDelCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_Alert)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.sql_table_design_col_more_dialog_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        col_del_btn.setOnClickListener {
            callback.onDel(col)
            dismiss()
        }
        col_name_tv.text = col.name
        col_edit_default_et.setText(col.default)
        col_edit_scale_et.setText(col.scale)
        col_edit_comment_et.setText(col.comment)
        if (col.type != SqlCol.TYPE_DECIMAL) {
            col_edit_scale_et.visibility = View.GONE
        } else {
            col_edit_scale_et.visibility = View.VISIBLE
        }
        col_edit_cancel.setOnClickListener { dismiss() }
        col_edit_sure.setOnClickListener {
            col.scale = col_edit_scale_et.text.toString()
            col.comment = col_edit_comment_et.text.toString()
            col.default = col_edit_default_et.text.toString()
            dismiss()
        }
    }

    fun bindCol(col: SqlCol, callback: ColumnDelCallback){
        this.col = col
        this.callback = callback
    }

    companion object {
        fun showDialog(context: Context, col: SqlCol, callback: ColumnDelCallback) {
            val dialog = ColMoreDialog()
            dialog.bindCol(col, callback)
            dialog.show((context as AppCompatActivity).fragmentManager, ColMoreDialog::class.java.simpleName)
        }
    }

    interface ColumnDelCallback{
        fun onDel(col: SqlCol)
    }
}