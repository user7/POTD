package com.geekbrains.potd.fragments.earth

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.geekbrains.potd.R
import com.geekbrains.potd.databinding.PhotoRecyclerItemBinding
import com.geekbrains.potd.fragments.utils.IdentityDiffUtil
import com.geekbrains.potd.nasa.EpicPhotosDTO

class EarthPhotosAdapter(private val context: Context) :
    RecyclerView.Adapter<EarthPhotosAdapter.ViewHolder>() {

    private var data = EpicPhotosDTO()
    fun setData(data: EpicPhotosDTO) {
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
        val photo = data[position]
        holder.binding.labelAbove.text = context.getString(R.string.photo_counter_and_date_label)
            .format(position + 1, data.size, photo.date)
        holder.binding.labelBelow.text = photo.caption
        val date = photo.date.split(" ")[0].replace("-", "/")
        val url = "https://epic.gsfc.nasa.gov/archive/natural/${date}/jpg/${photo.image}.jpg"
        holder.binding.imageView.load(url)
    }

    override fun getItemCount() = data.size
}