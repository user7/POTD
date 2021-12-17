package com.geekbrains.potd.utils

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import java.lang.Integer.min

class CustomImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, style: Int = 0):
    AppCompatImageView(context, attrs, style)
{
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val m = min(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(m, m)
    }
}