package com.geekbrains.potd.fragments.mars

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.geekbrains.potd.R
import com.geekbrains.potd.databinding.PhotoRecyclerItemBinding
import com.geekbrains.potd.nasa.MarsPhotosDTO

class MarsPhotosAdapter : RecyclerView.Adapter<MarsPhotosAdapter.ViewHolder>() {
    private var data: MarsPhotosDTO? = null
    fun setData(photos: MarsPhotosDTO?) {
        data = photos
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = PhotoRecyclerItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photos = data!!.photos // data can't be null in binding
        val p = photos[position]
        holder.binding.labelAbove.text = "${position + 1}/${photos.size}\n${p.date}"
        holder.binding.labelBelow.text = p.camera?.fullName ?: ""
        holder.binding.imageView.load(p.url)
    }

    override fun getItemCount(): Int = data?.photos?.size ?: 0
}