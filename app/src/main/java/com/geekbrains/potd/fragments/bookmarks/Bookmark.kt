package com.geekbrains.potd.fragments.bookmarks

import com.geekbrains.potd.nasa.EpicPhotosDTO
import com.geekbrains.potd.nasa.PotdDTO
import kotlin.collections.ArrayList

sealed class Bookmark {
    data class Potd(val data: PotdDTO, val apiDate: String) : Bookmark()
    data class Epic(val data: EpicPhotosDTO, val apiDate: String, val curPosition: Int) : Bookmark()
}

typealias Bookmarks = ArrayList<Bookmark>