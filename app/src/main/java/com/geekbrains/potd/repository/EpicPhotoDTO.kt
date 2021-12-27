package com.geekbrains.potd.repository

data class EpicPhotoDTO(
    val identifier: String,
    val caption: String,
    val image: String,
    val version: String,
    val date: String,
)