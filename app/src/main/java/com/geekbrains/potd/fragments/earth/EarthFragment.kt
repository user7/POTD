package com.geekbrains.potd.fragments.earth

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
import com.geekbrains.potd.databinding.FragmentEarthBinding
import com.geekbrains.potd.fragments.BookmarkableFragmentBase
import com.geekbrains.potd.fragments.bookmarks.Bookmark

class EarthFragment : BookmarkableFragmentBase() {
    private var _binding: FragmentEarthBinding? = null
    private val binding: FragmentEarthBinding get() = _binding!!
    private lateinit var adapter: EarthPhotosAdapter

    private val earthViewModel: EarthViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEarthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        earthViewModel.state.observe(viewLifecycleOwner) { renderState(it) }

        binding.chipNextDay.setOnClickListener { earthViewModel.adjustDate(1) }
        binding.chipPrevDay.setOnClickListener { earthViewModel.adjustDate(-1) }
        binding.chipResetDate.setOnClickListener { earthViewModel.resetDate() }

        binding.bookmarkCheckbox.setOnCheckedChangeListener { _, on -> setBookmarkedState(on) }

        adapter = EarthPhotosAdapter(requireContext())
        binding.recyclerView.adapter = adapter
        PagerSnapHelper().attachToRecyclerView(binding.recyclerView)
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    updateStateCurrentPosition()
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        val bookmark = startingBookmark as? Bookmark.Epic
        if (bookmark != null) {
            earthViewModel.setState(
                EarthViewModel.RequestState.Success(bookmark.data, bookmark.curPosition)
            )
            earthViewModel.nasaDate.setFromApiDate(bookmark.apiDate)
        } else {
            earthViewModel.sendServerRequest()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun renderState(state: EarthViewModel.RequestState) {
        when (state) {
            is EarthViewModel.RequestState.Error -> {
                binding.description.text = state.error
            }
            is EarthViewModel.RequestState.Loading -> {
                binding.recyclerView.visibility = View.INVISIBLE
                binding.description.setText(R.string.label_description_loading)
                binding.bookmarkCheckbox.visibility = View.GONE
            }
            is EarthViewModel.RequestState.Success -> {
                val photos = state.response
                if (photos.size == 0) {
                    binding.description.text = getString(R.string.no_photos_for_date)
                        .format(earthViewModel.nasaDate.format())
                } else {
                    adapter.setData(photos)
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.bookmarkCheckbox.visibility = View.VISIBLE
                    binding.description.text = ""
                    binding.recyclerView.scrollToPosition(state.currentPosition)
                    updateBookmarkCheckbox()
                }
            }
        }
    }

    private fun makeBookmark(): Bookmark.Epic? {
        return (earthViewModel.state.value as? EarthViewModel.RequestState.Success)
            ?.let {
                Bookmark.Epic(
                    it.response,
                    earthViewModel.nasaDate.format(),
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
        (earthViewModel.state.value as? EarthViewModel.RequestState.Success)?.let { state ->
            (binding.recyclerView.layoutManager as? LinearLayoutManager)?.let { recycler ->
                state.currentPosition = recycler.findFirstVisibleItemPosition()
                updateBookmarkCheckbox()
            }
        }
    }
}