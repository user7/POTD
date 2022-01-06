package com.geekbrains.potd.demo

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.Float.max

class NestedBehavior(context: Context? = null, attr: AttributeSet? = null) :
    CoordinatorLayout.Behavior<View>(context, attr) {
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean = dependency !== child && dependency is FloatingActionButton

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val dep = dependency as FloatingActionButton
        child.y = max(dep.y - child.height * 5 / 4, child.height / 16f)
        child.x = dep.x
        return super.onDependentViewChanged(parent, child, dependency)
    }
}