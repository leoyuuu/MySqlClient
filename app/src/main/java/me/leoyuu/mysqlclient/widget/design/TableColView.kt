package me.leoyuu.mysqlclient.widget.design

import android.content.Context
import android.support.v7.widget.ListPopupWindow
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*

import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.sql.SqlCol

/**
 * date 2018/3/1
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class TableColView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private val nameEt: EditText
    private val sizeEt: EditText
    private val typeTv: TextView
    private val nonnullCb: CheckBox
    private val primaryKeyCb: CheckBox

    private var col  = SqlCol.DEFAULT
    private lateinit var holder:TableColItemHolder

    init {
        LayoutInflater.from(context).inflate(R.layout.sql_table_design_item_content_view, this)
        nameEt = findViewById(R.id.table_col_name_et)
        sizeEt = findViewById(R.id.table_col_len_et)
        typeTv = findViewById(R.id.table_col_type_tv)
        nonnullCb = findViewById(R.id.table_col_nonnull_cb)
        primaryKeyCb = findViewById(R.id.table_col_primary_key_cb)

        typeTv.setOnClickListener {
            val listPopupWindow = ListPopupWindow(it.context)
            listPopupWindow.width = ViewGroup.LayoutParams.WRAP_CONTENT
            listPopupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            val adapter = ArrayAdapter<String>(context, R.layout.text_item_black_12, SqlCol.TYPE_LIST)
            listPopupWindow.setAdapter(adapter)
            listPopupWindow.anchorView = typeTv
            if (typeTv.text.isNotEmpty()){
                (0 until SqlCol.TYPE_LIST.size).forEach {
                    if (typeTv.text == SqlCol.TYPE_LIST[it]){
                        listPopupWindow.setSelection(it)
                    }
                }
            }
            listPopupWindow.isModal = true
            listPopupWindow.setOnItemClickListener { _, _, position, _ ->
                if (typeTv.text != SqlCol.TYPE_LIST[position]){
                    typeTv.text = SqlCol.TYPE_LIST[position]
                    col.type = SqlCol.TYPE_LIST[position]
                    if (listPopupWindow.isShowing){
                        listPopupWindow.dismiss()
                    }
                }
            }
            listPopupWindow.show()
        }

        primaryKeyCb.setOnCheckedChangeListener { _, isChecked -> col.primaryKey = isChecked }
        nonnullCb.setOnCheckedChangeListener { _, isChecked -> col.nonnull = isChecked }
        nameEt.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                col.name = s.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        sizeEt.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                col.size = if (s.isNullOrEmpty()) "" else s.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        findViewById<ImageButton>(R.id.table_col_more_btn).setOnClickListener {
            ColMoreDialog.showDialog(context, col, object : ColMoreDialog.ColumnDelCallback{
                override fun onDel(col: SqlCol) {
                    holder.onDel()
                }
            })
        }
    }

    fun onBind(col: SqlCol, holder: TableColItemHolder) {
        this.col = col
        nameEt.setText(col.name)
        sizeEt.setText(col.size)
        typeTv.text = col.type
        nonnullCb.isChecked = col.nonnull
        primaryKeyCb.isChecked = col.primaryKey

        this.holder = holder
    }

    val sql:String
    get() {
        var res = "${nameEt.text} ${typeTv.text}"
        if (sizeEt.text.isNotEmpty()){
            res += "(${sizeEt.text})"
        }
        if (nonnullCb.isChecked){
            res += " NOT NULL"
        }
        return res + "\n"
    }

    val primaryKey
    get() = primaryKeyCb.isChecked
}
