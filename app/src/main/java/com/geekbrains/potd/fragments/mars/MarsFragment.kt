package com.geekbrains.potd.fragments.mars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.potd.MainViewModel
import com.geekbrains.potd.R
import com.geekbrains.potd.databinding.FragmentMarsBinding
import com.geekbrains.potd.fragments.utils.BookmarkableFragmentBase
import com.geekbrains.potd.fragments.bookmarks.Bookmark

class MarsFragment : BookmarkableFragmentBase() {
    private var _binding: FragmentMarsBinding? = null
    private val binding: FragmentMarsBinding get() = _binding!!
    private lateinit var adapter: MarsPhotosAdapter

    private val marsViewModel: MarsViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        marsViewModel.state.observe(viewLifecycleOwner) { renderState(it) }

        binding.chipNextDay.setOnClickListener { marsViewModel.adjustDate(1) }
        binding.chipPrevDay.setOnClickListener { marsViewModel.adjustDate(-1) }
        binding.chipResetDate.setOnClickListener { marsViewModel.resetDate() }

        binding.bookmarkCheckbox.setOnCheckedChangeListener { _, on -> setBookmarkedState(on) }

        adapter = MarsPhotosAdapter(requireContext())
        binding.recyclerView.adapter = adapter
        PagerSnapHelper().attachToRecyclerView(binding.recyclerView)
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    updateStateCurrentPosition()
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        val bookmark = startingBookmark as? Bookmark.Mars
        if (bookmark != null) {
            marsViewModel.setState(
                MarsViewModel.RequestState.Success(bookmark.data, bookmark.curPosition)
            )
            marsViewModel.nasaDate.setFromApiDate(bookmark.apiDate)
        } else {
            marsViewModel.sendServerRequest()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun renderState(state: MarsViewModel.RequestState) {
        when (state) {
            is MarsViewModel.RequestState.Error -> {
                binding.description.text = state.error
            }
            is MarsViewModel.RequestState.Loading -> {
                binding.recyclerView.visibility = View.INVISIBLE
                binding.description.setText(R.string.label_description_loading)
                binding.bookmarkCheckbox.visibility = View.GONE
            }
            is MarsViewModel.RequestState.Success -> {
                adapter.setData(state.response)
                if (state.response.photos.isEmpty()) {
                    binding.bookmarkCheckbox.visibility = View.GONE
                    binding.description.text = getString(R.string.no_photos_for_date)
                        .format(marsViewModel.nasaDate.format())
                } else {
                    binding.description.text = ""
                    binding.bookmarkCheckbox.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.recyclerView.scrollToPosition(state.currentPosition)
                }
                updateBookmarkCheckbox()
            }
        }
    }

    private fun makeBookmark(): Bookmark.Mars? {
        return (marsViewModel.state.value as? MarsViewModel.RequestState.Success)
            ?.let {
                Bookmark.Mars(
                    it.response,
                    marsViewModel.nasaDate.format(),
                    it.currentPosition
                )
            }
    }

    private fun setBookmarkedState(on: Boolean) =
        makeBookmark()?.let { activityViewModel.editBookmark(it, on) }

    private fun updateBookmarkCheckbox() {
        makeBookmark()?.let {
            binding.bookmarkCheckbox.isChecked = activityViewModel.isBookmarkPresent(it)
        }
    }

    private fun updateStateCurrentPosition() {
        (marsViewModel.state.value as? MarsViewModel.RequestState.Success)?.let { state ->
            (binding.recyclerView.layoutManager as? LinearLayoutManager)?.let { recycler ->
                state.currentPosition = recycler.findFirstVisibleItemPosition()
                updateBookmarkCheckbox()
            }
        }
    }
}