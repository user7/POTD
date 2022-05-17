package com.geekbrains.potd.nasa

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class MarsCameraDTO(
    @field:SerializedName("full_name") val fullName: String?,
    @field:SerializedName("name") val shortName: String?,
)