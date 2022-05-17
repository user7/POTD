package com.geekbrains.potd.fragments.utils

import androidx.fragment.app.Fragment
import com.geekbrains.potd.fragments.bookmarks.Bookmark

// База для фрагментов, поддерживающих закладки (System, Earth, Mars)
abstract class BookmarkableFragmentBase : Fragment() {

    // При запуске фрагмент должен проверить закладку на валидность и показать контент,
    // соответствующий закладке
    var startingBookmark : Bookmark? = null
}