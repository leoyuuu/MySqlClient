package me.leoyuu.mysqlclient.widget

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * date 2018/2/26
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

class SpaceItemDecorator : RecyclerView.ItemDecoration() {
    private val outRect = Rect()

    fun setOutRect(outRect: Rect){
        this.outRect.set(outRect)
    }

    fun setOffset(top:Int = 0, bottom:Int = 0, left:Int = 0, right:Int = 0){
        outRect.top = top
        outRect.bottom = bottom
        outRect.right = right
        outRect.left = left
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(this.outRect)
    }
}
