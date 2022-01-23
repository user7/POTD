package com.geekbrains.potd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.potd.fragments.bookmarks.Bookmark
import com.geekbrains.potd.fragments.bookmarks.Bookmarks

class MainViewModel : ViewModel() {
    var themeId: Int? = null

    private val mutableBookmarks = MutableLiveData(Bookmarks())
    val bookmarks: LiveData<Bookmarks> = mutableBookmarks

    fun setBookmarks(bookmarksData: Bookmarks) = mutableBookmarks.postValue(bookmarksData)
    fun getBookmarks() = bookmarks.value ?: Bookmarks()

    fun editBookmark(b: Bookmark, add: Boolean) {
        val cont = bookmarks.value
        if (cont != null) {
            if (add) {
                if (!cont.contains(b))
                    setBookmarks(Bookmarks(cont).apply { add(b) })
            } else {
                if (cont.contains(b))
                    setBookmarks(Bookmarks(cont.filter { it !== b }))
            }
        }
    }

    fun isBookmarkPresent(b: Bookmark): Boolean = bookmarks.value?.contains(b) ?: false

}