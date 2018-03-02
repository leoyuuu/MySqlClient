package me.leoyuu.mysqlclient.module.log

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_log.*
import me.leoyuu.mysqlclient.R
import me.leoyuu.mysqlclient.store.DbHelper
import me.leoyuu.mysqlclient.store.entity.RecorderModel

class LogActivity : AppCompatActivity() {

    private val adapter = Adapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
        record_list.layoutManager = LinearLayoutManager(this)
        record_list.adapter = adapter

        initData()
    }

    private fun initData() {
        DbHelper.getRecorderAsync(object : DbHelper.OnQueryCallback<RecorderModel> {
            override fun onResult(result: List<RecorderModel>) {
                adapter.add(result)
            }
        })
    }
}
