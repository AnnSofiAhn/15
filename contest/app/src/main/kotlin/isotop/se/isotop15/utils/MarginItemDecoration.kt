package isotop.se.isotop15.utils

import android.content.res.Resources
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import isotop.se.isotop15.R

/**
 * @author Ann-Sofi Åhn
 *
 * Created on 17/02/13.
 */
class MarginItemDecoration(resources: Resources): RecyclerView.ItemDecoration() {

    val offset = resources.getDimensionPixelSize(R.dimen.card_margin)

    override fun getItemOffsets(outRect: Rect,
                                view: View?,
                                parent: RecyclerView?,
                                state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(offset, offset, offset, offset)
    }
}
