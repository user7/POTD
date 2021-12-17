package com.geekbrains.potd.utils

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import java.lang.Integer.max

class CustomImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, style: Int = 0):
    AppCompatImageView(context, attrs, style)
{
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val m = max(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, m)
    }
}