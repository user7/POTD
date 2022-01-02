package com.geekbrains.potd.nasa

data class EpicPhotoDTO(
    val identifier: String,
    val caption: String,
    val image: String,
    val version: String,
    val date: String,
)