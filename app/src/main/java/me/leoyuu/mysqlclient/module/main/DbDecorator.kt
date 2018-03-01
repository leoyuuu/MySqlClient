package me.leoyuu.mysqlclient.module.main

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import me.leoyuu.mysqlclient.util.Util

/**
 * date 2018/2/26
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class DbDecorator : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildViewHolder(view) is DbHolder){
            outRect.top = Util.dp2pix(8)
        }
    }
}
