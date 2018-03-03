package me.leoyuu.mysqlclient.widget.dialog

import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_confirm.*

import me.leoyuu.mysqlclient.R

/**
 * date 2018/2/27
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class DialogConfirm : DialogFragment() {
    private lateinit var content: String
    private lateinit var callback: ConfirmCallback

    private var title: String? = null
    private var sureString: String? = null
    private var cancelString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_Alert)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.dialog_confirm, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        confirm_dialog_content_tv.text = content
        if (title != null){
            confirm_dialog_title_tv.text = title
        }
        if (sureString != null){
            confirm_ok_btn.text = sureString
        }
        if (cancelString != null){
            confirm_cancel_btn.text = cancelString
        }

        confirm_ok_btn.setOnClickListener {
            callback.onSure()
            dismiss()
        }
        confirm_cancel_btn.setOnClickListener {
            callback.onCancel()
            dismiss()
        }
    }

    fun set(content:String, callback: ConfirmCallback, title: String? = null, sureString: String? = null, cancelString: String? = null){
        this.content = content
        this.callback = callback
        this.title = title
        this.sureString = sureString
        this.cancelString = cancelString
    }

    companion object {
        fun showDialog(context: Context, content:String, callback: ConfirmCallback, title: String? = null, sureString: String? = null, cancelString: String? = null) {
            val dialog = DialogConfirm()
            dialog.set(content, callback, title, sureString, cancelString)
            dialog.show((context as AppCompatActivity).fragmentManager, title)
        }
    }

    interface ConfirmCallback {
        fun onCancel()
        fun onSure()
    }
}