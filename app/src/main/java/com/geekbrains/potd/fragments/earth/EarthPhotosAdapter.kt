package com.geekbrains.potd.fragments.earth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.geekbrains.potd.R
import com.geekbrains.potd.databinding.PhotoRecyclerItemBinding
import com.geekbrains.potd.nasa.EpicPhotosDTO

class EarthPhotosAdapter(private val photos: EpicPhotosDTO) :
    RecyclerView.Adapter<EarthPhotosAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = PhotoRecyclerItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = photos[position]
        holder.binding.labelAbove.text = "${position+1}/${photos.size}\n${p.date}"
        holder.binding.labelBelow.text = p.caption
        val date = p.date.split(" ")[0].replace("-", "/")
        holder.binding.imageView.load("https://epic.gsfc.nasa.gov/archive/natural/${date}/jpg/${p.image}.jpg")
    }

    override fun getItemCount() = photos.size
}