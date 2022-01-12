package com.geekbrains.potd

import androidx.lifecycle.ViewModel
import com.geekbrains.potd.fragments.bookmarks.Bookmark
import com.geekbrains.potd.fragments.bookmarks.Bookmarks

class MainViewModel : ViewModel() {
    var themeId: Int? = null
    var bookmarks = Bookmarks()

    fun editBookmark(b: Bookmark, add: Boolean) {
        if (add) {
            if (!bookmarks.contains(b)) {
                bookmarks.add(b)
            }
        } else {
            bookmarks.remove(b)
        }
    }

    fun isBookmarkPresent(b: Bookmark) = bookmarks.contains(b)
}