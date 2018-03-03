package me.leoyuu.mysqlclient.widget.dialog

import android.app.DialogFragment
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_loading.*
import me.leoyuu.mysqlclient.R

/**
 * date 2018/3/3
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class DialogLoading : DialogFragment() {
    private var init = false

    var title: String = ""
        set(value) {
            if (init) {
                if (Thread.currentThread() == Looper.getMainLooper().thread) {
                    loading_title_tv.text = title
                } else {
                    loading_title_tv.post { loading_title_tv.text = title }
                }
            }
            field = value
        }

    var max: Int = Int.MIN_VALUE
        set(value) {
            if (init && value != Int.MIN_VALUE) {

                if (Thread.currentThread() == Looper.getMainLooper().thread) {
                    loading_h_progress_bar.max = value
                    showProgress()
                } else {
                    loading_h_progress_bar.post {
                        loading_h_progress_bar.max = value
                        showProgress()
                    }
                }
            }
            field = value
        }

    var current: Int = 0
        set(value) {
            if (init) {
                if (Thread.currentThread() == Looper.getMainLooper().thread) {
                    loading_tip_tv.text = getString(R.string.loading_progress, value, max)
                    loading_h_progress_bar.progress = value
                } else {
                    loading_tip_tv.post {
                        loading_tip_tv.text = getString(R.string.loading_progress, value, max)
                        loading_h_progress_bar.progress = value
                    }
                }
                loading_tip_tv.text = getString(R.string.loading_progress, value, max)
                loading_h_progress_bar.progress = value
            }
            field = value
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init = true
        loading_title_tv.text = title
        if (max != Int.MIN_VALUE) {
            showProgress()
        }
    }

    private fun showProgress() {
        loading_tip_tv.visibility = View.VISIBLE
        loading_h_progress_bar.visibility = View.VISIBLE
        loading_h_progress_bar.max = max
        loading_progress_bar.visibility = View.GONE
    }
}
