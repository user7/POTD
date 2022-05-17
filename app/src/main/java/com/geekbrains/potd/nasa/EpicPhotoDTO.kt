package com.geekbrains.potd.nasa

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class EpicPhotoDTO(
    val identifier: String,
    val caption: String,
    val image: String,
    val version: String,
    val date: String,
)

typealias EpicPhotosDTO = ArrayList<EpicPhotoDTO>