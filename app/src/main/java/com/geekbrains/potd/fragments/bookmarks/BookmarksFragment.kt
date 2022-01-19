package com.geekbrains.potd.fragments.bookmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.geekbrains.potd.MainViewModel
import com.geekbrains.potd.databinding.FragmentBookmarksBinding
import com.geekbrains.potd.fragments.BookmarkableFragmentBase

class BookmarksFragment(navigate: (Bookmark) -> Unit) : BookmarkableFragmentBase() {
    private val mainViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentBookmarksBinding? = null
    private val binding: FragmentBookmarksBinding get() = _binding!!
    private val adapter = BookmarksAdapter(navigate)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        adapter.data = mainViewModel.bookmarks
        binding.bookmarksRecyclerView.adapter = adapter
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}