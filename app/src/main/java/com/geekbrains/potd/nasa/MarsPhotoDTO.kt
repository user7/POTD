package com.geekbrains.potd.nasa

import com.google.gson.annotations.SerializedName

data class MarsPhotoDTO(
    @field:SerializedName("img_src") val url: String?,
    @field:SerializedName("earth_date") val date: String?,
    @field:SerializedName("camera") val camera: MarsCameraDTO?,
)