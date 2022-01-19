package com.geekbrains.potd.fragments.bookmarks

import com.geekbrains.potd.nasa.EpicPhotosDTO
import com.geekbrains.potd.nasa.MarsPhotosDTO
import com.geekbrains.potd.nasa.PotdDTO
import kotlin.collections.ArrayList

sealed class Bookmark {
    data class Potd(val data: PotdDTO, val apiDate: String) : Bookmark()
    data class Epic(val data: EpicPhotosDTO, val apiDate: String, val curPosition: Int) : Bookmark()
    data class Mars(val data: MarsPhotosDTO, val apiDate: String, val curPosition: Int) : Bookmark()
}

typealias Bookmarks = ArrayList<Bookmark>