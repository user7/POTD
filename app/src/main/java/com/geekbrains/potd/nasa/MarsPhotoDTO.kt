package com.geekbrains.potd.nasa

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class MarsPhotoDTO(
    @field:SerializedName("img_src") val url: String?,
    @field:SerializedName("earth_date") val date: String?,
    @field:SerializedName("camera") val camera: MarsCameraDTO?,
)