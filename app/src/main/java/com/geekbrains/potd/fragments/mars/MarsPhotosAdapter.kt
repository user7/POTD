package com.geekbrains.potd.fragments.mars

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.geekbrains.potd.R
import com.geekbrains.potd.databinding.PhotoRecyclerItemBinding
import com.geekbrains.potd.fragments.utils.IdentityDiffUtil
import com.geekbrains.potd.nasa.MarsPhotosDTO

class MarsPhotosAdapter(private val context: Context) :
    RecyclerView.Adapter<MarsPhotosAdapter.ViewHolder>() {

    private var data = MarsPhotosDTO(ArrayList())
    fun setData(data: MarsPhotosDTO) {
        this.data = data
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = PhotoRecyclerItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photos = data.photos
        val photo = photos[position]
        holder.binding.labelAbove.text = context.getString(R.string.photo_counter_and_date_label)
            .format(position + 1, photos.size, photo.date)
        holder.binding.labelBelow.text = photo.camera?.fullName ?: ""
        holder.binding.imageView.load(photo.url)
    }

    override fun getItemCount() = data.photos.size
}