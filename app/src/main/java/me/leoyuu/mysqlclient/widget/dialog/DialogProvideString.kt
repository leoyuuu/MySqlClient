package me.leoyuu.mysqlclient.widget.dialog

import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_provide_string.*

import me.leoyuu.mysqlclient.R

/**
 * date 2018/2/27
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class DialogProvideString : DialogFragment() {
    private lateinit var content: String
    private lateinit var callback: ConfirmCallback
    private lateinit var title: String

    private var contentValue:String ? = null
    private var sureString: String? = null
    private var cancelString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_Alert)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.dialog_provide_string, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dialog_p_s_content_lay.hint = content
        dialog_p_s_title_tv.text = title
        if (sureString != null){
            dialog_p_s_ok_btn.text = sureString
        }
        if (cancelString != null){
            dialog_p_s_cancel_btn.text = cancelString
        }

        dialog_p_s_content_et.setText(contentValue)

        dialog_p_s_ok_btn.setOnClickListener {
            callback.onSure(dialog_p_s_content_et.text.toString())
            dismiss()
        }
        dialog_p_s_cancel_btn.setOnClickListener {
            callback.onCancel();
            dismiss()
        }
    }

    fun set(content:String, title: String, callback: ConfirmCallback, sureString: String? = null, cancelString: String? = null, contentValue: String?){
        this.content = content
        this.callback = callback
        this.title = title
        this.sureString = sureString
        this.cancelString = cancelString
        this.contentValue = contentValue
    }

    companion object {
        fun showDialog(context: Context, content:String, title: String, callback: ConfirmCallback, sureString: String? = null, cancelString: String? = null, contentValue:String? = null) {
            val dialog = DialogProvideString()
            dialog.set(content, title, callback, sureString, cancelString, contentValue)
            dialog.show((context as AppCompatActivity).fragmentManager, title)
        }
    }

    interface ConfirmCallback {
        fun onCancel()
        fun onSure(content: String)
    }
}