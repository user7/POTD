package com.geekbrains.potd.nasa

import com.google.gson.annotations.SerializedName

data class MarsCameraDTO(
    @field:SerializedName("full_name") val fullName: String?,
    @field:SerializedName("name") val shortName: String?,
)