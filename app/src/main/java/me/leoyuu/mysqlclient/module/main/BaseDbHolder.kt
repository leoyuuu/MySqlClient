package me.leoyuu.mysqlclient.module.main

import android.support.v7.widget.RecyclerView
import android.view.View
import me.leoyuu.mysqlclient.model.BaseModel

/**
 * date 2018/2/26
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

open class BaseDbHolder(v: View) : RecyclerView.ViewHolder(v){
    lateinit var adapter:DbAdapter

    open fun onBind(model:BaseModel, adapter:DbAdapter){
        this.adapter = adapter
    }
}