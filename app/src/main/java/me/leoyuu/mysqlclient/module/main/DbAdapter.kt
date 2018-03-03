package me.leoyuu.mysqlclient.module.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.model.BaseModel
import me.leoyuu.mysqlclient.model.DbModel
import me.leoyuu.mysqlclient.model.TableModel

/**
 * date 2018/2/26
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class DbAdapter:RecyclerView.Adapter<BaseDbHolder>() {
    private val models = mutableListOf<BaseModel>()


    fun addDb(db:DbModel){
        models.add(db)
        notifyItemInserted(models.size-1)
    }

    fun clear(){
        val size = models.size
        models.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun removeItem(index:Int){
        val model = models.removeAt(index)
        if (model is TableModel){
            model.dbModel.tableList.remove(model)
            notifyItemRemoved(index)
        } else if (model is DbModel) {
            if (model.tableStatus == DbModel.TABLE_STATUS_SHOW) {
                models.removeAll(model.tableList)
                notifyItemRangeRemoved(index, model.tableList.size + 1)
            } else {
                notifyItemRemoved(index)
            }
        }
    }

    override fun getItemCount() = models.size

    override fun onBindViewHolder(holder: BaseDbHolder?, position: Int) {
        holder?.onBind(models[position], this)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseDbHolder {
        return if (viewType == DATA_TYPE_DB) {
            DbHolder(LayoutInflater.from(parent!!.context).inflate(viewType, parent, false))
        } else {
            TableHolder(LayoutInflater.from(parent!!.context).inflate(viewType, parent, false))
        }
    }

    override fun getItemViewType(position: Int) = when(models[position]) {
        is DbModel -> DATA_TYPE_DB
        else -> DATA_TYPE_TABLE
    }

    fun notifyDbTableStatusChange(position:Int){
        val model = models[position]
        if (getItemViewType(position) == DATA_TYPE_DB && model is DbModel){
            if (model.tableStatus == DbModel.TABLE_STATUS_SHOW){
                model.tableStatus = DbModel.TABLE_STATUS_HIDE
                models.removeAll(model.tableList)
                notifyItemRangeRemoved(position+1, model.tableList.size)
            } else {
                if (position == models.size-1){
                    models.addAll(model.tableList)
                } else {
                    models.addAll(position+1, model.tableList)
                }
                notifyItemRangeInserted(position+1, model.tableList.size)
                model.tableStatus = DbModel.TABLE_STATUS_SHOW
            }
        }
    }

    companion object {
        private const val DATA_TYPE_DB = R.layout.db_item
        private const val DATA_TYPE_TABLE = R.layout.table_item
    }
}