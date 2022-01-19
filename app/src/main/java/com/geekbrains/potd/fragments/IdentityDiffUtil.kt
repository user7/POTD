package com.geekbrains.potd.fragments

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

object IdentityDiffUtil {
    fun<VH : RecyclerView.ViewHolder, T> notifyDataChanged(
        oldData: ArrayList<T>,
        newData: ArrayList<T>,
        adapter: RecyclerView.Adapter<VH>
    ) {
        val callback = object : DiffUtil.Callback() {
            override fun getOldListSize() = oldData.size
            override fun getNewListSize() = newData.size
            override fun areItemsTheSame(op: Int, np: Int) = oldData[op] === newData[np]
            override fun areContentsTheSame(op: Int, np: Int) = areItemsTheSame(op, np)
        }
        val diff = DiffUtil.calculateDiff(callback)
        diff.dispatchUpdatesTo(adapter)
    }
}