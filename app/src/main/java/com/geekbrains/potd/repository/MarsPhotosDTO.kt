package com.geekbrains.potd.repository

import com.google.gson.annotations.SerializedName

data class MarsPhotosDTO(
    @field:SerializedName("photos") val photos: ArrayList<MarsPhotoDTO>,
)