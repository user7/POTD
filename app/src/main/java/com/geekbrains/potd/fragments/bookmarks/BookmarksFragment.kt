package com.geekbrains.potd.fragments.bookmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.potd.MainViewModel
import com.geekbrains.potd.databinding.FragmentBookmarksBinding
import com.geekbrains.potd.fragments.utils.BookmarkableFragmentBase

class BookmarksFragment() : BookmarkableFragmentBase() {
    private val mainViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentBookmarksBinding? = null
    private val binding: FragmentBookmarksBinding get() = _binding!!
    private lateinit var adapter: BookmarksAdapter
    private val itemTouchHelper = ItemTouchHelper(TouchHelperCallback())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        adapter = BookmarksAdapter(this, mainViewModel.getBookmarks(), requireContext())
        binding.bookmarksRecyclerView.adapter = adapter
        itemTouchHelper.attachToRecyclerView(binding.bookmarksRecyclerView)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class TouchHelperCallback :
        ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

        override fun onMove(
            recyclerView: RecyclerView,
            dragged: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            adapter.swapItems(dragged.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {} // unused
    }

}