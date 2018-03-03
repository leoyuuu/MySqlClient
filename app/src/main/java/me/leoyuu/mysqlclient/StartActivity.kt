package me.leoyuu.mysqlclient

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_start.*
import me.leoyuu.mysqlclient.sql.MySql
import me.leoyuu.mysqlclient.sql.ResultCallback
import me.leoyuu.mysqlclient.sql.SqlResult
import me.leoyuu.mysqlclient.util.JumpUtil
import me.leoyuu.mysqlclient.util.PrefUtil
import me.leoyuu.mysqlclient.util.Util
import me.leoyuu.mysqlclient.widget.dialog.DialogLoading

/**
 * date 2018/2/25
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class StartActivity : AppCompatActivity() {

    private var loading = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        password_et.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        connect_btn.setOnClickListener { attemptLogin() }

        val host = PrefUtil.getString(PrefUtil.KEY_HOST)
        val port = PrefUtil.getInt(PrefUtil.KEY_PORT)
        val user = PrefUtil.getString(PrefUtil.KEY_USER)

        if (host.isNotEmpty()) {
            host_et.setText(host)
        }
        if (port > 0) {
            port_et.setText(port.toString())
        }

        if (user.isNotEmpty()) {
            user_et.setText(user)
        }
    }

    private fun attemptLogin(){
        if (loading) return

        host_et.error = null
        port_et.error = null
        user_et.error = null
        password_et.error = null

        if (host_et.text.isEmpty()){
            host_et.error = "主机不能为空"
            return
        }

        if (port_et.text.isEmpty()){
            port_et.error = "主机不能为空"
            return
        }

        if (user_et.text.isEmpty()){
            user_et.error = "用户名不能为空"
            return
        }
        loading = true
        val dialog = DialogLoading()
        dialog.title = "连接中"
        dialog.show(fragmentManager, "start")
        MySql.init(host_et.text.toString(), port_et.text.toString().toInt(), user_et.text.toString(), password_et.text.toString(), object : ResultCallback{
            override fun onResult(result: SqlResult) {
                if (result.sqlOK){
                    PrefUtil.putString(PrefUtil.KEY_HOST, host_et.text.toString())
                    PrefUtil.putInt(PrefUtil.KEY_PORT, port_et.text.toString().toInt())
                    PrefUtil.putString(PrefUtil.KEY_USER, user_et.text.toString())
                    JumpUtil.gotoMainActivity(this@StartActivity)
                    this@StartActivity.finish()
                } else {
                    Util.showToast(result.errMsg)
                }
                dialog.dismiss()
                loading = false
            }
        })
    }
}
