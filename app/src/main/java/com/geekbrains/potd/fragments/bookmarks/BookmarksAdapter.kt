package com.geekbrains.potd.fragments.bookmarks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.potd.databinding.FragmentBookmarksItemPotdBinding
import com.geekbrains.potd.fragments.NavFragment
import java.lang.IllegalArgumentException

class BookmarksAdapter(private val navigate: (Bookmark) -> Unit) :
    RecyclerView.Adapter<BookmarksAdapter.BookmarkHolder>() {

    var data = Bookmarks()
        set(data_) {
            field = data_
            notifyDataSetChanged()
        }

    open class BookmarkHolder(view: View) : RecyclerView.ViewHolder(view)
    enum class BookmarkType {
        POTD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkHolder {
        when (viewType) {
            BookmarkType.POTD.ordinal -> {
                val binding = FragmentBookmarksItemPotdBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return PotdBookmarkHolder(binding)
            }
            else -> throw IllegalArgumentException("Unsupported viewType=$viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is Bookmark.Potd -> BookmarkType.POTD.ordinal
        }
    }

    override fun onBindViewHolder(holder: BookmarkHolder, position: Int) {
        when (holder) {
            is PotdBookmarkHolder -> {
                val potd = data[position] as Bookmark.Potd
                holder.binding.bookmarkDate.text = potd.apiDate
                holder.binding.bookmarkText.text = potd.data.title
                holder.bookmark = potd
            }
        }
    }

    override fun getItemCount(): Int = data.size

    inner class PotdBookmarkHolder(val binding: FragmentBookmarksItemPotdBinding) :
        BookmarkHolder(binding.root) {

        var bookmark: Bookmark.Potd? = null

        init {
            binding.root.setOnClickListener { gotoBookmark() }
        }

        private fun gotoBookmark() {
            bookmark?.let { navigate(it) }
        }
    }
}