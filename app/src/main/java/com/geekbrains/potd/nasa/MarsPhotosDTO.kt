package com.geekbrains.potd.nasa

import com.google.gson.annotations.SerializedName

data class MarsPhotosDTO(
    @field:SerializedName("photos") val photos: ArrayList<MarsPhotoDTO>,
)