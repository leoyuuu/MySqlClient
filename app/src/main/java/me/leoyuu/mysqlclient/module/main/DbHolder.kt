package me.leoyuu.mysqlclient.module.main

import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.model.BaseModel
import me.leoyuu.mysqlclient.model.DbModel
import me.leoyuu.mysqlclient.sql.MySql
import me.leoyuu.mysqlclient.sql.ResultCallback
import me.leoyuu.mysqlclient.sql.SqlResult
import me.leoyuu.mysqlclient.util.JumpUtil
import me.leoyuu.mysqlclient.util.Util
import me.leoyuu.mysqlclient.widget.dialog.DialogConfirm

/**
 * date 2018/2/26
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class DbHolder(v: View) : BaseDbHolder(v){
    private val dbName = v.findViewById<TextView>(R.id.db_name)
    private val dbMore = v.findViewById<ImageButton>(R.id.db_more)
    private lateinit var model:DbModel
    init {
        v.setOnClickListener { adapter.notifyDbTableStatusChange(position = adapterPosition) }
        dbMore.setOnClickListener { showPopDbWindow() }
    }

    override fun onBind(model: BaseModel, adapter: DbAdapter) {
        super.onBind(model, adapter)
        if (model is DbModel){
            this.model = model
            dbName.text = model.name
        }
    }

    private fun showPopDbWindow(){
        val dbMenu = PopupMenu(dbMore.context, dbMore)
        dbMenu.inflate(R.menu.menu_db_more)
        dbMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_db_add_table -> {
                    JumpUtil.gotoTableDesignActivity(itemView.context, model.name)
                }
                R.id.action_db_del -> {
                    DialogConfirm.showDialog(itemView.context, "确认删除数据库 ${model.name}？ 删除数据库后无法恢复。", object : DialogConfirm.ConfirmCallback{
                        override fun onCancel() {}
                        override fun onSure() {
                            MySql.getSql()?.delDb(model.name, object : ResultCallback{
                                override fun onResult(result: SqlResult) {
                                    if (result.sqlOK) {
                                        Util.showToast("删除成功")
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