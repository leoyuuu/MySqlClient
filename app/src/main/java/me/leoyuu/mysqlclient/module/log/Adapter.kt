package me.leoyuu.mysqlclient.module.log

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.store.entity.RecorderModel

/**
 * date 2018/3/2
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class Adapter : RecyclerView.Adapter<Holder>() {

    private val list = mutableListOf<RecorderModel>()

    fun add(records: List<RecorderModel>) {
        val start = list.size
        list.addAll(records)
        notifyItemRangeInserted(start, records.size)
    }

    fun remove(index: Int) {
        list.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.sql_log_item, parent, false))
    }
}
