package vanson.dev.instagramclone.views

import android.content.Context
import android.util.AttributeSet

class SquareImageView(context: Context, attrs: AttributeSet) : androidx.appcompat.widget.AppCompatImageView(context, attrs){
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) { //to fix size of image View
        super.onMeasure(widthMeasureSpec, widthMeasureSpec) // -> square
    }
}