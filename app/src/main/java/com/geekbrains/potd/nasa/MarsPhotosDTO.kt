package com.geekbrains.potd.nasa

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class MarsPhotosDTO(
    @field:SerializedName("photos") val photos: ArrayList<MarsPhotoDTO>,
)