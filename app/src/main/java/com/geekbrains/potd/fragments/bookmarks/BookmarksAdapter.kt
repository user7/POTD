package com.geekbrains.potd.fragments.bookmarks

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.method.MovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import androidx.viewbinding.ViewBinding
import com.geekbrains.potd.App
import com.geekbrains.potd.R
import com.geekbrains.potd.databinding.FragmentBookmarksItemEpicBinding
import com.geekbrains.potd.databinding.FragmentBookmarksItemMarsBinding
import com.geekbrains.potd.databinding.FragmentBookmarksItemPotdBinding
import java.lang.IllegalArgumentException

class BookmarksAdapter(
    private val parentFragment: BookmarksFragment,
    private val data: Bookmarks,
    private val context: Context
) :
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
                return PotdBookmarkHolder(binding, parentFragment, this)
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
            is PotdBookmarkHolder -> holder.bind(data[position] as Bookmark.Potd)
            is EpicBookmarkHolder -> holder.bind(data[position] as Bookmark.Epic)
            is MarsBookmarkHolder -> holder.bind(data[position] as Bookmark.Mars)
            else -> {
                TODO("unknown holder type $holder in BookmarksAdapter::onBindViewHolder")
            }
        }
    }

    override fun getItemCount(): Int = data.size

    open inner class GenericHolder<B : ViewBinding>(val binding: B) : BookmarkHolder(binding.root) {
        var bookmark: Bookmark? = null
        var expanded: Boolean = false

        init {
            binding.root.setOnClickListener { bookmark?.let { App.instance.navigator?.navigate(it) } }
        }
    }

    inner class PotdBookmarkHolder(
        binding: FragmentBookmarksItemPotdBinding,
        private val parentFragment: BookmarksFragment,
        private val adapter: BookmarksAdapter
    ) :
        GenericHolder<FragmentBookmarksItemPotdBinding>(binding) {
        init {
            binding.deleteBookmarkButton.setOnClickListener { adapter.deleteItem(adapterPosition) }
            binding.moreInfoButton.setOnClickListener { handleExpandButton() }
        }

        private fun handleExpandButton() {
            expanded = !expanded
            if (expanded) {
                TransitionManager.beginDelayedTransition(
                    binding.root,
                    TransitionSet().addTransition(ChangeBounds()).addTransition(Fade())
                )
                binding.bookmarkTitle.visibility = View.INVISIBLE
                binding.bookmarkTitleLower.visibility = View.VISIBLE
                binding.bookmarkDescription.visibility = View.VISIBLE
                binding.moreInfoButton.setImageResource(R.drawable.ic_arrow_drop_up_24)
            } else {
                TransitionManager.beginDelayedTransition(
                    binding.root,
                    TransitionSet().addTransition(ChangeBounds())
                )
                binding.bookmarkTitle.visibility = View.VISIBLE
                binding.bookmarkDescription.visibility = View.GONE
                binding.bookmarkTitleLower.visibility = View.GONE
                binding.moreInfoButton.setImageResource(R.drawable.ic_arrow_drop_down_24)
            }
        }

        fun bind(bookmark: Bookmark.Potd) {
            this.bookmark = bookmark
            binding.bookmarkDate.text = bookmark.apiDate
            binding.bookmarkTitle.text = bookmark.data.title

            binding.bookmarkTitleLower.text = constructTitleSpannable()
            binding.bookmarkTitleLower.movementMethod = LinkMovementMethod.getInstance()
            binding.bookmarkTitleLower.isClickable = true

            binding.bookmarkDescription.text = constructDescriptionSpannable()
            binding.bookmarkDescription.movementMethod = LinkMovementMethod.getInstance()
            binding.bookmarkDescription.isClickable = true
        }

        private fun constructDescriptionSpannable(): SpannableString {
            val bookmark = (this.bookmark as? Bookmark.Potd)!!
            val descLabel = context.getString(R.string.span_desc)
            val editLabel = context.getString(R.string.span_edit)
            val descText = (bookmark.data.explanation ?: "") + " "
            val desc = SpannableString(descLabel + descText + editLabel)
            desc.setSpan(
                StyleSpan(Typeface.BOLD), 0, descLabel.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            val textEnd = descLabel.length + descText.length
            desc.setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) = handleEditDesc()
                },
                textEnd, desc.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            desc.setSpan(
                ForegroundColorSpan(Color.BLUE),
                textEnd, desc.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return desc
        }

        private fun constructTitleSpannable(): SpannableString {
            val bookmark = (this.bookmark as? Bookmark.Potd)!!
            val descLabel = context.getString(R.string.span_title)
            val editLabel = context.getString(R.string.span_edit)
            val descText = (bookmark.data.title ?: "") + " "
            val desc = SpannableString(descLabel + descText + editLabel)
            desc.setSpan(
                StyleSpan(Typeface.BOLD), 0, descLabel.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            val textEnd = descLabel.length + descText.length
            desc.setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) = handleEditTitle()
                },
                textEnd, desc.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            desc.setSpan(
                ForegroundColorSpan(Color.BLUE),
                textEnd, desc.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return desc
        }

        fun getDescription(): String = (bookmark as? Bookmark.Potd)?.data?.explanation ?: ""
        fun setDescription(desc: String) {
            val potd = (bookmark as? Bookmark.Potd)!!
            adapter.replaceBookmark(
                adapterPosition,
                potd.copy(data = potd.data.copy(explanation = desc))
            )
            binding.bookmarkDescription.text = constructDescriptionSpannable()
        }

        fun getTitle(): String = (bookmark as? Bookmark.Potd)?.data?.title ?: ""
        fun setTitle(title: String) {
            val potd = (bookmark as? Bookmark.Potd)!!
            adapter.replaceBookmark(
                adapterPosition,
                potd.copy(data = potd.data.copy(title = title))
            )
            binding.bookmarkTitle.text = title
            binding.bookmarkTitleLower.text = constructTitleSpannable()
        }

        fun handleEditDesc() {
            val editFragment = EditDialogFragment(
                "Description",
                getDescription(),
                object : EditDialogFragment.EditTextCallback {
                    override fun submitEditedText(t: String) {
                        setDescription(t)
                    }
                })
            editFragment.show(parentFragment.parentFragmentManager, null)
        }

        fun handleEditTitle() {
            val editFragment = EditDialogFragment(
                "Title",
                getTitle(),
                object : EditDialogFragment.EditTextCallback {
                    override fun submitEditedText(t: String) {
                        setTitle(t)
                    }
                })
            editFragment.show(parentFragment.parentFragmentManager, null)
        }
    }

    private fun replaceBookmark(adapterPosition: Int, bookmark: Bookmark) {
        data[adapterPosition] = bookmark
        notifyItemChanged(adapterPosition)
    }

    inner class EpicBookmarkHolder(
        binding: FragmentBookmarksItemEpicBinding,
        adapter: BookmarksAdapter
    ) :
        GenericHolder<FragmentBookmarksItemEpicBinding>(binding) {
        init {
            binding.deleteBookmarkButton.setOnClickListener { adapter.deleteItem(adapterPosition) }
        }

        fun bind(bookmark: Bookmark.Epic) {
            binding.bookmarkDate.text = bookmark.apiDate
            binding.bookmarkTitle.text = bookmark.data.getOrNull(bookmark.curPosition)?.caption
            binding.bookmarkPhotoIndex.text =
                context.getString(R.string.photo_counter)
                    .format(bookmark.curPosition + 1, bookmark.data.size)
            this.bookmark = bookmark
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

        fun bind(bookmark: Bookmark.Mars) {
            binding.bookmarkDate.text = bookmark.apiDate
            val camera = bookmark.data.photos.getOrNull(bookmark.curPosition)?.camera
            binding.bookmarkTitle.text = camera?.fullName ?: ""
            binding.bookmarkPhotoIndex.text =
                context.getString(R.string.photo_counter)
                    .format(bookmark.curPosition + 1, bookmark.data.photos.size)
            this.bookmark = bookmark
        }
    }

    fun deleteItem(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    fun swapItems(posFrom: Int, posTo: Int) {
        data.removeAt(posFrom).apply { data.add(posTo, this) }
        notifyItemMoved(posFrom, posTo)
    }
}