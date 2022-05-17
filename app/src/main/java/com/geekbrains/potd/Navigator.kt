package com.geekbrains.potd

import com.geekbrains.potd.fragments.bookmarks.Bookmark

interface Navigator {
    fun navigate(bookmark: Bookmark)
}