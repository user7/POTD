package com.geekbrains.potd.view.api.earth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.geekbrains.potd.R
import com.geekbrains.potd.repository.EpicPhotosDTO

class EarthPhotosAdapter(private val photos: EpicPhotosDTO) :
    RecyclerView.Adapter<EarthPhotosAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val labelAbove: TextView
        val imageView: ImageView
        val labelBelow: TextView

        init {
            labelAbove = view.findViewById(R.id.labelAbove) as TextView
            imageView = view.findViewById(R.id.imageView) as ImageView
            labelBelow = view.findViewById(R.id.labelBelow) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.earth_recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = photos[position]
        holder.labelAbove.setText("${position + 1}/${photos.size} ${p.date}")
        holder.labelBelow.setText(p.caption)
        val date = p.date.split(" ")[0].replace("-", "/")
        holder.imageView.load("https://epic.gsfc.nasa.gov/archive/natural/${date}/jpg/${p.image}.jpg")
    }

    override fun getItemCount() = photos.size
}