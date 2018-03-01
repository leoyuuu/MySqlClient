package me.leoyuu.mysqlclient.module.main

import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.model.BaseModel
import me.leoyuu.mysqlclient.model.TableModel
import me.leoyuu.mysqlclient.sql.MySql
import me.leoyuu.mysqlclient.sql.ResultCallback
import me.leoyuu.mysqlclient.sql.SqlResult
import me.leoyuu.mysqlclient.util.JumpUtil
import me.leoyuu.mysqlclient.util.Util
import me.leoyuu.mysqlclient.widget.dialog.DialogConfirm
import me.leoyuu.mysqlclient.widget.dialog.DialogProvideString

/**
 * date 2018/2/26
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class TableHolder(v: View) : BaseDbHolder(v){
    private lateinit var tableModel:TableModel

    private val tableNameTv:TextView = v.findViewById(R.id.table_name)
    private val tableMoreBtn:ImageButton = v.findViewById(R.id.table_more)

    init {
        tableNameTv.setOnClickListener { JumpUtil.gotoTableDetailActivity(v.context, tableModel.dbModel.name, tableModel.tableName) }
        tableMoreBtn.setOnClickListener { showPopTableWindow() }
    }

    override fun onBind(model: BaseModel, adapter: DbAdapter) {
        super.onBind(model, adapter)
        if (model is TableModel){
            tableModel = model
            tableNameTv.text = model.tableName
        }
    }


    private fun showPopTableWindow(){
        val dbMenu = PopupMenu(tableMoreBtn.context, tableMoreBtn)
        dbMenu.inflate(R.menu.menu_table_more)
        dbMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_table_alter_name -> {
                    DialogProvideString.showDialog(itemView.context, "表名", "修改表名", contentValue = tableModel.tableName, callback = object : DialogProvideString.ConfirmCallback {
                        override fun onCancel() {}
                        override fun onSure(content: String) {
                            MySql.getSql()?.alterTableName(tableModel.dbModel.name, tableModel.tableName, content, object : ResultCallback{
                                override fun onResult(result: SqlResult) {
                                    if (result.sqlOK){
                                        tableModel.tableName = content
                                        tableNameTv.text = content
                                    } else {
                                        Util.showToast(result.errMsg)
                                    }
                                }
                            })
                        }
                    })
                }
//                R.id.action_table_design -> {
//                    JumpUtil.gotoTableDesignActivity(itemView.context, tableModel.dbModel.name, tableModel.tableName)
//                }
                R.id.action_table_del -> {
                    DialogConfirm.showDialog(itemView.context, content = "确认删除表${tableModel.dbModel.name}.${tableModel.tableName}吗？\n删除后无法恢复", callback = object : DialogConfirm.ConfirmCallback{
                        override fun onCancel() {}

                        override fun onSure() {
                            MySql.getSql()?.deleteTable(tableModel.dbModel.name, tableModel.tableName, object :ResultCallback{
                                override fun onResult(result: SqlResult) {
                                    if (result.sqlOK){
                                        adapter.removeItem(adapterPosition)
                                    } else {
                                        Util.showToast(result.errMsg)
                                    }
                                }
                            })
                        }
                    })
                }
            }
            true
        }
        dbMenu.show()
    }
}