package com.geekbrains.potd.fragments.bookmarks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.geekbrains.potd.databinding.FragmentBookmarksItemEpicBinding
import com.geekbrains.potd.databinding.FragmentBookmarksItemMarsBinding
import com.geekbrains.potd.databinding.FragmentBookmarksItemPotdBinding
import com.geekbrains.potd.fragments.Navigator
import java.lang.IllegalArgumentException

class BookmarksAdapter(private val navigator: Navigator, val data: Bookmarks) :
    RecyclerView.Adapter<BookmarksAdapter.BookmarkHolder>() {

    open class BookmarkHolder(view: View) : RecyclerView.ViewHolder(view)
    enum class BookmarkType {
        POTD,
        EPIC,
        MARS,
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkHolder {
        when (viewType) {
            BookmarkType.POTD.ordinal -> {
                val binding = FragmentBookmarksItemPotdBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return PotdBookmarkHolder(binding, this)
            }
            BookmarkType.EPIC.ordinal -> {
                val binding = FragmentBookmarksItemEpicBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return EpicBookmarkHolder(binding, this)
            }
            BookmarkType.MARS.ordinal -> {
                val binding = FragmentBookmarksItemMarsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return MarsBookmarkHolder(binding, this)
            }
            else -> throw IllegalArgumentException("Unsupported viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is Bookmark.Potd -> BookmarkType.POTD.ordinal
            is Bookmark.Epic -> BookmarkType.EPIC.ordinal
            is Bookmark.Mars -> BookmarkType.MARS.ordinal
        }
    }

    override fun onBindViewHolder(holder: BookmarkHolder, position: Int) {
        when (holder) {
            is PotdBookmarkHolder -> {
                val bookmark = data[position] as Bookmark.Potd
                holder.binding.bookmarkDate.text = bookmark.apiDate
                holder.binding.bookmarkText.text = bookmark.data.title
                holder.bookmark = bookmark
            }
            is EpicBookmarkHolder -> {
                val bookmark = data[position] as Bookmark.Epic
                holder.binding.bookmarkDate.text = bookmark.apiDate
                holder.binding.bookmarkText.text = bookmark.data.getOrNull(0)?.caption
                holder.bookmark = bookmark
            }
            is MarsBookmarkHolder -> {
                val bookmark = data[position] as Bookmark.Mars
                holder.binding.bookmarkDate.text = bookmark.apiDate
                val camera = bookmark.data.photos.getOrNull(0)?.camera
                holder.binding.bookmarkText.text = camera?.shortName ?: ""
                holder.bookmark = bookmark
            }
            else -> {
                TODO("unknown holder type $holder in BookmarksAdapter::onBindViewHolder")
            }
        }
    }

    override fun getItemCount(): Int = data.size

    open inner class GenericHolder<B : ViewBinding>(val binding: B) : BookmarkHolder(binding.root) {
        var bookmark: Bookmark? = null

        init {
            binding.root.setOnClickListener { bookmark?.let { navigator.navigate(it) } }
        }
    }

    inner class PotdBookmarkHolder(
        binding: FragmentBookmarksItemPotdBinding,
        adapter: BookmarksAdapter
    ) :
        GenericHolder<FragmentBookmarksItemPotdBinding>(binding) {
        init {
            binding.deleteBookmarkButton.setOnClickListener { adapter.deleteItem(adapterPosition) }
        }
    }

    inner class EpicBookmarkHolder(
        binding: FragmentBookmarksItemEpicBinding,
        adapter: BookmarksAdapter
    ) :
        GenericHolder<FragmentBookmarksItemEpicBinding>(binding) {
        init {
            binding.deleteBookmarkButton.setOnClickListener { adapter.deleteItem(adapterPosition) }
        }
    }

    inner class MarsBookmarkHolder(
        binding: FragmentBookmarksItemMarsBinding,
        adapter: BookmarksAdapter
    ) :
        GenericHolder<FragmentBookmarksItemMarsBinding>(binding) {
        init {
            binding.deleteBookmarkButton.setOnClickListener { adapter.deleteItem(adapterPosition) }
        }
    }

    fun deleteItem(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }
}