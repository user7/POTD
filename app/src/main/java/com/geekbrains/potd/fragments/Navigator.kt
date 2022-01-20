package com.geekbrains.potd.fragments

import com.geekbrains.potd.fragments.bookmarks.Bookmark

interface Navigator {
    fun navigate(bookmark: Bookmark)
}